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
import java.util.Map;
import java.util.Optional;
import javax.imageio.ImageIO;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import java.util.Random;

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

        // Handle avatar file upload
        if (avatarFile != null && !avatarFile.isEmpty()) {
            // Save to src/main/resources/static/img/ so it works in both dev and prod
            String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/img/";
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
            user.setAvatarUrl("/img/default-avatar.png");
        }
        userService.saveUser(user);

        // Add cache-busting parameter to force browser to reload the image
        return "redirect:/profile?updated=" + System.currentTimeMillis();
    }

    @PostMapping("/update")
    public String updateProfile(Authentication authentication, Model model,
                                @ModelAttribute("userUpdateDTO")  @Valid UserUpdateDTO userUpdate) {
        String useName = authentication.getName();
        Optional<Users> usersOptional = userService.findUserByUsername(useName);
        Users user = usersOptional.get();
        userMapper.updateUser(user,userUpdate);
        userService.saveUser(user);
        model.addAttribute("user", user);
        System.out.println(userUpdate.toString());
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

    @PostMapping("/send-confirmation-code")
    @ResponseBody
    public ResponseEntity<?> sendConfirmationCode(Authentication authentication, HttpSession session) {
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
        if (user == null || user.getEmail() == null) {
            return ResponseEntity.badRequest().body("User not found or email missing");
        }
        // Generate 6-digit code
        String code = String.format("%06d", new Random().nextInt(1000000));
        // Store code in session
        session.setAttribute("confirmationCode", code);
        // Send code to email (implement sendEmail in your UserService or MailService)
        try {
            userService.sendConfirmationCodeEmail(user.getEmail(), code);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to send email");
        }
    }

    @PostMapping("/verify-confirmation-code")
    @ResponseBody
    public Map<String, Object> verifyConfirmationCode(@RequestBody Map<String, String> payload, HttpSession session) {
        String inputCode = payload.get("code");
        String sessionCode = (String) session.getAttribute("confirmationCode");
        boolean success = sessionCode != null && sessionCode.equals(inputCode);
        return Map.of("success", success);
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam(required = false) String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 @RequestParam(required = false) String confirmationCode,
                                 Authentication authentication,
                                 HttpSession session,
                                 Model model) {
        // Get user
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
            model.addAttribute("error", "User not found.");
            return "UserDashboard/Profile";
        }
        // If user has a password, require confirmation code and current password
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            String sessionCode = (String) session.getAttribute("confirmationCode");
            if (sessionCode == null || !sessionCode.equals(confirmationCode)) {
                model.addAttribute("error", "Invalid or expired confirmation code.");
                return "UserDashboard/Profile";
            }
            if (currentPassword == null || !userService.checkPassword(user, currentPassword)) {
                model.addAttribute("error", "Current password is incorrect.");
                return "UserDashboard/Profile";
            }
        }
        // For users without a password, skip confirmation code and current password
        // Check new password match
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "New passwords do not match.");
            return "UserDashboard/Profile";
        }
        // Change password
        userService.changeUserPassword(user, newPassword);
        // Optionally clear confirmation code from session
        session.removeAttribute("confirmationCode");
        model.addAttribute("success", "Password changed successfully.");
        return "UserDashboard/Profile";
    }



}