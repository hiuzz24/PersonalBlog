package com.he181180.personalblog.controller;

import com.he181180.personalblog.entity.Users;
import com.he181180.personalblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;

    @GetMapping
    public String adminDashboard(Authentication authentication,
                                 Model model) {
        if(authentication == null || !authentication.isAuthenticated()){
            return "redirect:/login";
        }

        String userName = authentication.getName();
        Optional<Users> users = userService.findUserByUsername(userName);
        model.addAttribute("user",users.get());
        return "AdminDashboard/dashboard";
    }

    @GetMapping("/userManagement")
    public String userManagement(Model model){
        List<Users> users = userService.getAllUsers();
        model.addAttribute("users",users);
        return "AdminDashboard/userManagement";
    }

    @RequestMapping("/search")
    public String search(@RequestParam("search") String search
                        ,Model model){
        List<Users> users = userService.findUserByFullNameOrUserName(search);
        model.addAttribute("users",users);
        return "AdminDashboard/userManagement";
    }

    @PostMapping("/updateRole/{id}")
    public String updateRole(@PathVariable("id") int userID,
                             @RequestParam("role") String role){
    userService.updateRole(userID,role);
    return "redirect:/admin/userManagement";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") int userID){
        userService.delete(userID);
        return "redirect:/admin/userManagement";
    }
}
