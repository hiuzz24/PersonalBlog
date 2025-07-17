package com.he181180.personalblog.controller;

import com.he181180.personalblog.entity.Posts;
import com.he181180.personalblog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/explore")
    public String explore(@RequestParam(defaultValue = "1") int page, Model model){
        int pageSize = 6;

        List<Posts> posts = postService.getPaginatedPosts(page, pageSize);
        int totalPosts = postService.getTotalPostCount();
        int totalPages = (int) Math.ceil((double) totalPosts / pageSize);

        model.addAttribute("allPost", posts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        return "explore";
    }

    @RequestMapping("/search")
    public String searchByTitleAndContent(@RequestParam("search") String search, Model model){
        List<Posts> posts = postService.searchPostByTitleAndContent(search);
        model.addAttribute("allPost", posts);
        model.addAttribute("currentPage", 1);
        model.addAttribute("totalPages", 1);

        model.addAttribute("searchKeyword", search);
        return "explore";
    }

    @RequestMapping("/tags/{tagID}")
    public String getPostsByTag(@PathVariable("tagID") int tagID, Model model){
        List<Posts> posts = postService.findPostsByTagID(tagID);
        model.addAttribute("allPost", posts);
        model.addAttribute("currentPage", 1);
        model.addAttribute("totalPages", 1);
        return "explore";
    }

}
