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

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;

    @GetMapping
    public String adminDashboard(Authentication authentication,
                                 Model model) {
        if(authentication == null || !authentication.isAuthenticated()){
            return "redirect:/login";
        }

        String userName = authentication.getName();
        Optional<Users> users = userService.findUserByUsername(userName);
        model.addAttribute("user",users.get());
        return "AdminDashboard/AdminDashboard";
    }

    @RequestMapping("/chart/postPerTag")
    @ResponseBody
    public Map<String,Integer> postPerUser(){
        return postService.getPostCountByTag();
    }

    @RequestMapping("/chart/top5Author")
    @ResponseBody
    public Map<String,Integer> top5Author(){
        return postService.top5Author();
    }

    @RequestMapping("/chart/postPerStatus")
    @ResponseBody
    public Map<String,Integer> postPerStatus(){
        return postService.postPerStatus();
    }

    @RequestMapping("/chart/postPerMonth")
    @ResponseBody
    public Map<String,Integer> postPerMonth(@RequestParam(value = "year",required = false) Integer year){
        if(year == null){
            year = LocalDate.now().getYear();
        }
        return postService.postPerMonth(year);
    }
}
