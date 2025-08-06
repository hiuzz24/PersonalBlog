package com.he181180.personalblog.controller;

import com.he181180.personalblog.DTO.UserUpdateDTO;
import com.he181180.personalblog.Mapper.UserMapper;
import com.he181180.personalblog.entity.Posts;
import com.he181180.personalblog.entity.Users;
import com.he181180.personalblog.service.CurrentUserService;
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

import java.io.File;
import java.io.IOException;


@Controller
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CurrentUserService currentUserService;

    @GetMapping
    public String profile(Authentication authentication, Model model) {
        Users user = null;
        if (authentication != null) {
           user = currentUserService.getCurrentUser(authentication);
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
            @RequestParam(value = "avatarUrl", required = false) String avatarUrl) throws IOException {

        Users user = null;
        if (authentication != null) {
            user = currentUserService.getCurrentUser(authentication);
        }
        if (user == null) {
            return "redirect:/profile?error=user-not-found";
        }

        // Handle avatar file upload
        if (avatarFile != null && !avatarFile.isEmpty()) {
            String uploadDir = System.getProperty("user.dir") + "/uploads/img/";
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            String fileName = System.currentTimeMillis() + "_" + avatarFile.getOriginalFilename();
            File dest = new File(dir, fileName);
            try {
                avatarFile.transferTo(dest);
                user.setAvatarUrl("/img/" + fileName);
                System.out.println("Avatar uploaded successfully: " + user.getAvatarUrl());
            } catch (IOException e) {
                e.printStackTrace();
                return "redirect:/profile?error=upload-failed";
            }
        } else if (avatarUrl != null && !avatarUrl.trim().isEmpty()) {
            user.setAvatarUrl(avatarUrl.trim());
        } else if (user.getAvatarUrl() == null || user.getAvatarUrl().isEmpty()) {
            // Set default avatar if none is set
            user.setAvatarUrl("/img/user.png");
        }
        userService.saveUser(user);

        // Add cache-busting parameter to force browser to reload the image
        return "redirect:/profile?updated=" + System.currentTimeMillis();
    }

    @PostMapping("/update")
    public String updateProfile(Authentication authentication, Model model,
                                @ModelAttribute("userUpdateDTO")  @Valid UserUpdateDTO userUpdate) {

        Users user = currentUserService.getCurrentUser(authentication);
        userMapper.updateUser(user,userUpdate);
        System.out.println(user.getAvatarUrl());
        userService.saveUser(user);
        model.addAttribute("user", user);
        String avatarSrc = (user.getAvatarUrl() != null && !user.getAvatarUrl().isEmpty())
                ? user.getAvatarUrl() + "?t=" + System.currentTimeMillis()
                : "/img/default-avatar.png";
        model.addAttribute("avatarSrc", avatarSrc);
        return "UserDashboard/Profile";
    }
}

