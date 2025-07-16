package com.he181180.personalblog.controller;

import com.he181180.personalblog.entity.Posts;
import com.he181180.personalblog.entity.Tags;
import com.he181180.personalblog.service.PostService;
import com.he181180.personalblog.service.TagService;
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


    @RequestMapping("/tags/{tagID}")
    public String getPostsByTag(@PathVariable("tagID") int tagID,
                                Model model){
        List<Posts> posts = postService.findPostsByTagID(tagID);
        model.addAttribute("allPost",posts);
        return "explore";
    }


}
