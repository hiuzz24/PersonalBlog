package com.he181180.personalblog.controller;

import com.he181180.personalblog.DTO.UserUpdateDTO;
import com.he181180.personalblog.Mapper.UserMapper;
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

    @Autowired
    private UserMapper userMapper;


    @GetMapping
    public String profile(Authentication authentication, Model model) {
        String username = authentication.getName();
        Optional<Users> user = userService.findUserByUsername(username);
        if(user.isPresent()) {
            model.addAttribute("user", user.get());
            model.addAttribute("userUpdateDTO", new UserUpdateDTO());
        }
        return "UserDashboard/Profile";
    }

    @PostMapping("/update")
    public String updateProfile(Authentication authentication, Model model,
                              @ModelAttribute("userUpdateDTO") UserUpdateDTO userUpdate) {
        String useName = authentication.getName();
        Optional<Users> usersOptional = userService.findUserByUsername(useName);
        Users user = usersOptional.get() ;
        userMapper.updateUser(user,userUpdate);
        userService.saveUser(user);
        model.addAttribute("user", user);
        System.out.println(user.toString());
        return "UserDashboard/Profile";
    }

}
