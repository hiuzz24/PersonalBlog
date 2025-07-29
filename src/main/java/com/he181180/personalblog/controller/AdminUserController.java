package com.he181180.personalblog.controller;

import com.he181180.personalblog.entity.Users;
import com.he181180.personalblog.service.PostService;
import com.he181180.personalblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/user")
public class AdminUserController {
    @Autowired
    private UserService userService;

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

    @GetMapping("/recover/{id}")
    public String recover(@PathVariable("id") int userID){
        userService.recover(userID);
        return "redirect:/admin/userManagement";
    }
}
