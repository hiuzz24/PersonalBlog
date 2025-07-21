package com.he181180.personalblog.controller;

import com.he181180.personalblog.DTO.UserUpdateDTO;
import com.he181180.personalblog.Mapper.UserMapper;
import com.he181180.personalblog.entity.Posts;
import com.he181180.personalblog.entity.Users;
import com.he181180.personalblog.service.PostService;
import com.he181180.personalblog.service.UserService;
import jakarta.validation.Valid;
import org.apache.tomcat.util.modeler.BaseAttributeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.Optional;
import javax.imageio.ImageIO;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private UserMapper userMapper;


    @GetMapping
    public String profile(Authentication authentication, Model model) {
        Users user = null;
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof org.springframework.security.oauth2.core.user.OAuth2User oauth2User) {
                String email = oauth2User.getAttribute("email");
                user = userService.findUserByEmail(email).orElse(null);
            } else {
                String username = authentication.getName();
                user = userService.findUserByUsername(username).orElse(null);
            }
            UserUpdateDTO userUpdateDTO = UserUpdateDTO.builder()
                    .email(user.getEmail())
                    .fullName(user.getFullName())
                    .bio(user.getBio())
                    .build();
            model.addAttribute("userUpdateDTO", userUpdateDTO);

        }
        if (user == null) {
            user = new Users();
            user.setUsername("Unknown");
            user.setFullName("Unknown");
            user.setEmail("unknown@example.com");
        }
        String avatarSrc = (user.getAvatarUrl() != null && !user.getAvatarUrl().isEmpty())
                ? user.getAvatarUrl() + "?t=" + System.currentTimeMillis()
                : "/img/default-avatar.png";
        model.addAttribute("user", user);
        model.addAttribute("avatarSrc", avatarSrc);
        return "UserDashboard/Profile";
    }



    @PostMapping("/updateAvatar")
    public String updateProfileAvatar(
            Authentication authentication,
            @RequestParam(value = "avatarFile", required = false) MultipartFile avatarFile,
            @RequestParam(value = "avatarUrl", required = false) String avatarUrl,
            @ModelAttribute("user") Users userUpdate) {

        Users user = null;
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof org.springframework.security.oauth2.core.user.OAuth2User oauth2User) {
                String email = oauth2User.getAttribute("email");
                user = userService.findUserByEmail(email).orElse(null);
            } else {
                String username = authentication.getName();
                user = userService.findUserByUsername(username).orElse(null);
            }
        }
        if (user == null) {
            return "redirect:/profile?error=user-not-found";
        }

        // Only update full name if provided
        if (userUpdate.getFullName() != null && !userUpdate.getFullName().trim().isEmpty()) {
            user.setFullName(userUpdate.getFullName());
        }
        user.setBio(userUpdate.getBio());

        if (avatarFile != null && !avatarFile.isEmpty()) {
            String savedUrl = saveFileSomewhere(avatarFile);
            if (savedUrl != null) {
                user.setAvatarUrl(savedUrl);
            }
        } else if (avatarUrl != null && !avatarUrl.trim().isEmpty()) {
            user.setAvatarUrl(avatarUrl.trim());
        }

        userService.updateUser(user);

        return "redirect:/profile";
    }

    @PostMapping("/update")
    public String updateProfile(Authentication authentication, Model model,
                                @ModelAttribute("userUpdateDTO")  @Valid UserUpdateDTO userUpdate) {
        String useName = authentication.getName();
        Optional<Users> usersOptional = userService.findUserByUsername(useName);
        Users user = usersOptional.get() ;
        userMapper.updateUser(user,userUpdate);
        userService.saveUser(user);
        model.addAttribute("user", user);
        System.out.println(userUpdate.toString());
        System.out.println(user.toString());
        return "UserDashboard/Profile";
    }

    private String saveFileSomewhere(MultipartFile file) {
        if (file == null || file.isEmpty()) return null;
        // Save to static/img/ so Spring Boot can serve it
        String uploadDir = "src/main/resources/static/img/";
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();
        String originalFilename = file.getOriginalFilename();
        String ext = originalFilename.substring(originalFilename.lastIndexOf('.') + 1).toLowerCase();
        String filename = System.currentTimeMillis() + "_avatar." + ext;
        File dest = new File(uploadDir + filename);
        try {
            BufferedImage originalImage = ImageIO.read(file.getInputStream());
            BufferedImage resizedImage = new BufferedImage(150, 150, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = resizedImage.createGraphics();
            g.drawImage(originalImage, 0, 0, 150, 150, null);
            g.dispose();
            ImageIO.write(resizedImage, ext, dest);
        } catch (IOException e) {
            // TODO: Use proper logging
            e.printStackTrace();
            return null;
        }
        // Return URL for static/img/
        return "/img/" + filename;
    }



}