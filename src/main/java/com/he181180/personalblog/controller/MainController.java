package com.he181180.personalblog.controller;

import com.he181180.personalblog.entity.PasswordResetToken;
import com.he181180.personalblog.entity.Users;
import com.he181180.personalblog.repository.PasswordResetTokenRepository;
import com.he181180.personalblog.repository.UserRepository;
import com.he181180.personalblog.security.CustomOAuth2User;
import com.he181180.personalblog.security.CustomUserPrincipal;
import com.he181180.personalblog.service.PostService;
import com.he181180.personalblog.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class MainController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index() {
        return "redirect:/explore";
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication,Model model) {
        String username = authentication.getName();
        Optional<Users> user = userService.findUserByUsername(username);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
        }
        return "UserDashboard/dashboard";
    }

    @PostMapping("/register")
    public String register(@RequestParam String fullName,
                           @RequestParam String username,
                           @RequestParam String email,
                           @RequestParam String password,
                           Model model) {
        boolean hasError = false;

        if (userRepository.findByUsernameAndDeletedFalse(username).isPresent()) {
            model.addAttribute("usernameError", "Username already exists");
            hasError = true;
        }
        if (userRepository.findByEmailAndDeletedFalse(email).isPresent()) {
            model.addAttribute("emailError", "Email already exists");
            hasError = true;
        }

        if (hasError) {
            model.addAttribute("fullName", fullName);
            model.addAttribute("username", username);
            model.addAttribute("email", email);
            return "register";
        }
        Users newUser = new Users();
        newUser.setFullName(fullName);
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setRole("WRITER");
        newUser.setAvatarUrl("/img/user.png");
        userRepository.save(newUser);
        return "redirect:/login";
    }

    @GetMapping("/GoogleLogin")
    public String googleLoginSuccess(@AuthenticationPrincipal CustomOAuth2User customOAuth2User, Model model) {
        if (customOAuth2User != null) {
            Users user = customOAuth2User.getUser();

            // If user does not have a username → ask for username
            if (user.getUsername() == null || user.getUsername().isEmpty()) {
                model.addAttribute("email", user.getEmail());
                return "complete-username"; // Redirect to a page to complete the username
            }
        }

        return "redirect:/explore";
    }

    @PostMapping("/complete-username")
    public String completeUsername(@RequestParam String email,
                                   @RequestParam String username,
                                   Model model,
                                   HttpServletRequest request) {
        // Check if username exists
        if (userRepository.findByUsername(username).isPresent()) {
            model.addAttribute("email", email);
            model.addAttribute("error", "Username already taken");
            return "complete-username";
        }

        // Update username
        Users user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            user.setUsername(username);
            userRepository.save(user);

            // Create new CustomUserPrincipal authentication
            CustomUserPrincipal customUserPrincipal = new CustomUserPrincipal(user);
            Authentication auth = new UsernamePasswordAuthenticationToken(
                    customUserPrincipal, null, customUserPrincipal.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        return "redirect:/profile";
    }

    @GetMapping("/forgotPassword")
    public String forgotPassword() {
        return "forgotPassword";
    }

    @PostMapping("/forgotPassword")
    public String forgotPassword(HttpServletRequest request,
                                 @RequestParam String email,
                                 RedirectAttributes redirectAttributes) {

        Users user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            redirectAttributes.addFlashAttribute("email", email);
            redirectAttributes.addFlashAttribute("emailError", "Email cannot be found. Please try again.");
            return "redirect:/forgotPassword";
        }

        String token = UUID.randomUUID().toString();
        PasswordResetToken myToken = new PasswordResetToken();
        myToken.setToken(token);
        myToken.setUser(user);
        passwordResetTokenRepository.save(myToken);

        String appUrl = request.getScheme() + "://" + request.getServerName() + ":" +
                request.getServerPort() + request.getContextPath();
        String url = appUrl + "/resetPassword?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Reset Password");
        message.setText("Click here to reset password:" + "\r\n" +
                url + "\r\n" +
                "The link will expire in 1 hour after the time sent of this email.");
        message.setFrom("no-reply.token-email@gmail.com");

        mailSender.send(message);

        redirectAttributes.addFlashAttribute("message", "A password reset link has been sent to " + user.getEmail() + ".");
        return "redirect:/forgotPassword";
    }

    @GetMapping("/resetPassword")
    public String tokenValidate(Model model,
                                @RequestParam("token") String token) {
        PasswordResetToken passToken = passwordResetTokenRepository.findByToken(token);
        Calendar cal = Calendar.getInstance();

        if (passToken == null) {
            model.addAttribute("tokenError", "The link is invalid: Invalid token.");
        } else if (passToken.getExpiryDate().before(cal.getTime())) {
            passwordResetTokenRepository.delete(passToken);
            model.addAttribute("tokenError", "The link is invalid: Token expired.");
        } else {
            model.addAttribute("token", token);
        }
        return "resetPassword";

    }

    @GetMapping("/savePassword")
    public String invalidAccess(){
        return "redirect:/login";
    }

    @PostMapping("/savePassword")
    public String savePassword(@RequestParam("newPassword") String newPassword,
                               @RequestParam("token") String token,
                               Model model) {

        PasswordResetToken passToken = passwordResetTokenRepository.findByToken(token);
        Calendar cal = Calendar.getInstance();
        boolean faultyToken = false;

        if (passToken == null) {
            model.addAttribute("tokenError", "Error: Invalid token.");
            faultyToken = true;
        } else if (passToken.getExpiryDate().before(cal.getTime())) {
            model.addAttribute("tokenError", "Error: Token expired and is deleted.");
            passwordResetTokenRepository.delete(passToken);
            faultyToken = true;
        }

        if(faultyToken) {return "resetPassword";}

        Users user = passToken.getUser();
        if (user != null) {
            userService.changeUserPassword(user, newPassword);
            passwordResetTokenRepository.delete(passToken);
            model.addAttribute("message", "Password reset successfully.");
            return "resetPassword";
        }

        model.addAttribute("tokenError", "Error: User cannot be found through token.");
        return "resetPassword";
    }

}
