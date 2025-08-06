package com.he181180.personalblog.controller;

import com.he181180.personalblog.DTO.PasswordChangeResponse;
import com.he181180.personalblog.entity.Users;
import com.he181180.personalblog.service.CurrentUserService;
import com.he181180.personalblog.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/change-password")
public class ChangePasswordController {
    @Autowired
    private UserService userService;

    @Autowired
    private CurrentUserService currentUserService;

    // Pattern to validate password
    private static final String PASSWORD_PATTERN =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$";

    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    @GetMapping
    public String showChangePasswordForm(Authentication authentication, Model model) {
        Users user = currentUserService.getCurrentUser(authentication);
        if (user == null) {
            // Instead of returning error, redirect to login or show a friendly message
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        // Render the change-password page instead of redirecting
        return "UserDashboard/change-password";
    }


    @PostMapping
    public ResponseEntity<PasswordChangeResponse> changePassword(
            @RequestParam(required = false) String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            @RequestParam(required = false) String confirmationCode,
            Authentication authentication,
            HttpSession session) {

        Users user = currentUserService.getCurrentUser(authentication);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new PasswordChangeResponse(false, "User not found.", "USER_NOT_FOUND"));
        }

        // Kiểm tra verification nếu user đã có password
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
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new PasswordChangeResponse(false, "Verification session has expired. Please verify your email again.", "VERIFICATION_EXPIRED"));
            }

            if (codeVerified == null || !codeVerified) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new PasswordChangeResponse(false, "Please verify your email first.", "EMAIL_NOT_VERIFIED"));
            }

            // Kiểm tra mật khẩu hiện tại
            if (currentPassword == null || !userService.checkPassword(user, currentPassword)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new PasswordChangeResponse(false, "Current password is incorrect.", "CURRENT_PASSWORD_INCORRECT"));
            }
        }

        // Kiểm tra pattern mật khẩu mới
        if (!pattern.matcher(newPassword).matches()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new PasswordChangeResponse(false, "Password must be at least 6 characters long and contain at least one uppercase letter, one lowercase letter, one number, and one special character.", "INVALID_PASSWORD_FORMAT"));
        }

        // Kiểm tra mật khẩu xác nhận
        if (!newPassword.equals(confirmPassword)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new PasswordChangeResponse(false, "New passwords do not match.", "PASSWORDS_NOT_MATCH"));
        }

        // Thay đổi mật khẩu
        userService.changeUserPassword(user, newPassword);

        // Xóa các session attributes
        session.removeAttribute("confirmationCode");
        session.removeAttribute("confirmationCodeTimestamp");
        session.removeAttribute("codeVerified");
        session.removeAttribute("codeVerifiedTimestamp");

        // Invalidate current session để buộc đăng nhập lại
        session.invalidate();
        return ResponseEntity.ok(new PasswordChangeResponse(true, "Password changed successfully. Please log in again with your new password.",null));
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
