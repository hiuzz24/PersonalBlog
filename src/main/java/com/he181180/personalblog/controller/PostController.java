package com.he181180.personalblog.controller;

import com.he181180.personalblog.entity.Posts;
import com.he181180.personalblog.entity.Users;
import com.he181180.personalblog.service.PostService;
import com.he181180.personalblog.service.TagService;
import com.he181180.personalblog.service.TagService;
import com.he181180.personalblog.service.UserService;
import org.aspectj.apache.bcel.generic.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private TagService tagService;

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

    @GetMapping("/PostDetail/{postID}")
    public String postDetail(@PathVariable("postID") int postID
            ,Model model){
        Posts posts = postService.findPostByPostID(postID);
        List<Integer> tagIDs = tagService.findTagIDByPostID(postID);
        List<Posts> postsList = tagIDs.stream().flatMap(tagID -> postService.findPostsByTagID(tagID).stream())
                .filter(p -> p.getPostID() != postID)
                .distinct()
                .collect(Collectors.toList());

        Collections.shuffle(postsList);
        List<Posts> randomFive = postsList.stream()
                .limit(5).collect(Collectors.toList());
        model.addAttribute("relatedPosts",randomFive);
        model.addAttribute("postDetail",posts);
        return "postDetail";
    }

}