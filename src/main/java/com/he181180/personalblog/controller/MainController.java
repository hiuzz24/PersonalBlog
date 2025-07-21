package com.he181180.personalblog.controller;

import com.he181180.personalblog.entity.Users;
import com.he181180.personalblog.repository.UserRepository;
import com.he181180.personalblog.service.PostService;
import com.he181180.personalblog.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.List;
import java.util.Optional;

@Controller
public class MainController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

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
        if(user.isPresent()) {
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

        if (userRepository.findByUsername(username).isPresent()) {
            model.addAttribute("usernameError", "Username already exists");
            hasError = true;
        }
        if (userRepository.findByEmail(email).isPresent()) {
            model.addAttribute("emailError", "Email already exists");
            hasError = true;
        }

        if (hasError) {
            return "register";
        }
        Users newUser = new Users();
        newUser.setFullName(fullName);
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setRole("writer");

        userRepository.save(newUser);
        return "redirect:/login";
    }

    @GetMapping("/GoogleLogin")
    public String googleLoginSuccess(@AuthenticationPrincipal OAuth2User oauth2User, Model model) {
        if (oauth2User != null) {
            String email = oauth2User.getAttribute("email");
            String name = oauth2User.getAttribute("name");

            // Find user by email
            Users user = userRepository.findByEmail(email).orElse(null);

            if (user == null) {
                user = new Users();
                user.setEmail(email);
                user.setFullName(name);
                user.setRole("writer");
                userRepository.save(user);
            }

            // If user does not have a username → ask for username
            if (user.getUsername() == null || user.getUsername().isEmpty()) {
                model.addAttribute("email", email);
                return "complete-username";
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

            // 👉 Recreate Authentication with the updated user
            Authentication auth = new UsernamePasswordAuthenticationToken(
                    user.getUsername(), null, List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        return "redirect:/profile";
    }
}
