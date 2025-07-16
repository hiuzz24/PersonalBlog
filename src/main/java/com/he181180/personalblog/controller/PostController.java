package com.he181180.personalblog.controller;

import com.he181180.personalblog.entity.Posts;
import com.he181180.personalblog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class PostController {
    @Autowired
    private PostService postService;

    @GetMapping("/explore")
    public String explore(Model model){
        model.addAttribute("allPost",postService.getAll());
        return "explore";
    }

    @RequestMapping("/search")
    public String searchByTitleAndContent(@RequestParam("search") String search,
                                          Model model){
        List<Posts> posts = postService.searchPostByTitleAndContent(search);
        model.addAttribute("allPost",posts);
        return "explore";
    }

    @ModelAttribute("recentPosts")
    public List<Posts> recentPosts(){
        return postService.findTop5RecentPosts();
    }


}
