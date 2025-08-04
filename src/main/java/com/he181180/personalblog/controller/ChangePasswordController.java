package com.he181180.personalblog.controller;

import com.he181180.personalblog.entity.Users;
import com.he181180.personalblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.util.Map;
import java.util.Random;

@Controller
public class ChangePasswordController {
    @Autowired
    private UserService userService;

    @GetMapping("/change-password")
    public String showChangePasswordForm(Authentication authentication, Model model) {
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
            // Instead of returning error, redirect to login or show a friendly message
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        // Render the change-password page instead of redirecting
        return "UserDashboard/change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam(required = false) String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 @RequestParam(required = false) String confirmationCode,
                                 Authentication authentication,
                                 HttpSession session,
                                 Model model) {
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
            model.addAttribute("user", null);
            return "UserDashboard/change-password";
        }

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            Boolean codeVerified = (Boolean) session.getAttribute("codeVerified");
            Long codeVerifiedTimestamp = (Long) session.getAttribute("codeVerifiedTimestamp");

            long currentTime = System.currentTimeMillis();
            boolean verificationExpired = codeVerifiedTimestamp == null ||
                                        (currentTime - codeVerifiedTimestamp) > 300000;

            if (verificationExpired) {
                session.removeAttribute("codeVerified");
                session.removeAttribute("codeVerifiedTimestamp");
                session.removeAttribute("confirmationCode");
                session.removeAttribute("confirmationCodeTimestamp");
                model.addAttribute("error", "Verification session has expired. Please verify your email again.");
                model.addAttribute("user", user);
                return "UserDashboard/change-password";
            }

            if (codeVerified == null || !codeVerified) {
                model.addAttribute("error", "Please verify your email first.");
                model.addAttribute("user", user);
                return "UserDashboard/change-password";
            }

            if (currentPassword == null || !userService.checkPassword(user, currentPassword)) {
                model.addAttribute("error", "Current password is incorrect.");
                model.addAttribute("user", user);
                return "UserDashboard/change-password";
            }
        }

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "New passwords do not match.");
            model.addAttribute("user", user);
            return "UserDashboard/change-password";
        }

        userService.changeUserPassword(user, newPassword);
        session.removeAttribute("confirmationCode");
        session.removeAttribute("confirmationCodeTimestamp");
        session.removeAttribute("codeVerified");
        session.removeAttribute("codeVerifiedTimestamp");

        model.addAttribute("message", "Password changed successfully. Please return to the login page and log in again.");
        model.addAttribute("user", user);
        return "login";
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
        // Store code in session with timestamp
        session.setAttribute("confirmationCode", code);
        session.setAttribute("confirmationCodeTimestamp", System.currentTimeMillis());
        // Remove any previous verification status
        session.removeAttribute("codeVerified");
        session.removeAttribute("codeVerifiedTimestamp");
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
        Long codeTimestamp = (Long) session.getAttribute("confirmationCodeTimestamp");

        long currentTime = System.currentTimeMillis();
        boolean codeExpired = codeTimestamp == null || (currentTime - codeTimestamp) > 300000;

        if (codeExpired) {
            session.removeAttribute("confirmationCode");
            session.removeAttribute("confirmationCodeTimestamp");
            return Map.of("success", false, "message", "Verification code has expired. Please request a new one.");
        }

        boolean success = sessionCode != null && sessionCode.equals(inputCode);

        if (success) {
            // Mark code as verified with timestamp
            session.setAttribute("codeVerified", true);
            session.setAttribute("codeVerifiedTimestamp", currentTime);
        }

        return Map.of("success", success);
    }
}
