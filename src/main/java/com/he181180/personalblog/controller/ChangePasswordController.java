package com.he181180.personalblog.controller;

import com.he181180.personalblog.entity.Users;
import com.he181180.personalblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;

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
            // Render the change-password page with error
            model.addAttribute("user", null);
            return "UserDashboard/change-password";
        }
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            String sessionCode = (String) session.getAttribute("confirmationCode");
            if (sessionCode == null || !sessionCode.equals(confirmationCode)) {
                model.addAttribute("error", "Invalid or expired confirmation code.");
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
        model.addAttribute("success", "Password changed successfully.");
        return "UserDashboard/change-password";
    }
}
