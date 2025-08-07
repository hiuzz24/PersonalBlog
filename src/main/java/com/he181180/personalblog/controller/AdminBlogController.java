package com.he181180.personalblog.controller;

import com.he181180.personalblog.entity.Posts;
import com.he181180.personalblog.entity.Users;
import com.he181180.personalblog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin/blog")
public class AdminBlogController {
    @Autowired
    private PostService postService;

    @GetMapping("/blogManagement")
    public String blogManagement(Model model){
        List<Posts> posts = postService.getAllPosts();
        model.addAttribute("posts",posts);
        return "AdminDashboard/blogManagement";
    }

    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable("id")int postID){
        postService.deletePost(postID);
        return "redirect:/admin/blog/blogManagement";
    }

    @RequestMapping("/recover/{id}")
    public String recover(@PathVariable("id")int postID){
        postService.recoverPost(postID);
        return "redirect:/admin/blog/blogManagement";
    }

}
