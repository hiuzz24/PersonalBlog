package com.he181180.personalblog.controller;

import com.he181180.personalblog.entity.Users;
import com.he181180.personalblog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MainController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

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
    public String googleLoginSuccess(@AuthenticationPrincipal OAuth2User oauth2User,
                                     Model model) {
        if (oauth2User != null) {
            model.addAttribute("name", oauth2User.getAttribute("name"));
        }
        return "redirect:/explore";
    }
}
