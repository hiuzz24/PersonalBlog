package com.he181180.personalblog.controller;

import com.he181180.personalblog.entity.Posts;
import com.he181180.personalblog.entity.Users;
import com.he181180.personalblog.service.PostService;
import com.he181180.personalblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Optional;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;
    @GetMapping
    public String profile(Authentication authentication, Model model) {
        String username = authentication.getName();
        Optional<Users> user = userService.findUserByUsername(username);        if(user.isPresent()) {
            model.addAttribute("user", user.get());
        }
        return "UserDashboard/Profile";
    }

    @PutMapping("/update")
    public Users updateProfile(Authentication authentication, Model model,
                              @ModelAttribute("user") Users userUpdate) {
        String useName = authentication.getName();
        Optional<Users> user = userService.findUserByUsername(useName);
        user.ifPresent(users -> model.addAttribute("user", users));
        Users user1 = Users.builder()
                .userID(user.get().getUserID())
                .fullName(userUpdate.getFullName())
                .email(userUpdate.getEmail())
                .password(userUpdate.getPassword())
                .build();
        return userService.updateUser(user1);
    }


}
