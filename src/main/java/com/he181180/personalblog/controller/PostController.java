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
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
@Controller
public class PostController {

    @Autowired
    private PostService postService;


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

    @GetMapping("/PostDetail/{postID}")
    public String postDetail(@PathVariable("postID") int postID
                            ,Model model){
        Posts posts = postService.findPostByPostID(postID);
        model.addAttribute("postDetail",posts);
        return "postDetail";
    }
}
