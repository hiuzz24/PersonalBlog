package com.he181180.personalblog.controller;

import com.he181180.personalblog.entity.Users;
import com.he181180.personalblog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MainController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/register")
    public String register(){
        return "register";
    }

    @GetMapping("/explore")
    public String explore(){
        return "explore";
    }

    @PostMapping("/register")
    public String register(@RequestParam String fullName,
                           @RequestParam String username,
                           @RequestParam String email,
                           @RequestParam String password,
                           Model model){
        if (userRepository.findByUsername(username).isPresent()) {
            model.addAttribute("usernameError", "Username already exists");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            model.addAttribute("emailError", "Email already exists");
        }

        if(!model.asMap().isEmpty()) {
            return "register";
        }else{
            Users newUser = new Users();
            newUser.setPassword(passwordEncoder.encode(password));
            newUser.setUsername(username);
            newUser.setEmail(email);
            newUser.setFullName(fullName);
            newUser.setRole("writer");
            userRepository.save(newUser);
            return "redirect:/login";
        }
    }
}
