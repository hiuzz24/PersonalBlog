package com.he181180.personalblog.controller;

import com.he181180.personalblog.entity.Posts;
import com.he181180.personalblog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin/moderate")
public class AdminModerateController {
    @Autowired
    private PostService postService;

    @GetMapping("/moderateNewBlogs")
    public String moderateNewBlog(Model model){
        List<Posts> allPostPending = postService.findAllPostPending();

        model.addAttribute("totalPending", allPostPending.size());
        model.addAttribute("approved", postService.countApproved());
        model.addAttribute("rejected", postService.countRejected());
        model.addAttribute("posts",allPostPending);
        model.addAttribute("rejectedTab", false);
        return "AdminDashboard/moderateNewBlogs";
    }

    @GetMapping("/moderateRejectedBlogs")
    public String moderateRejectedBlogs(Model model){
        List<Posts> rejectedPosts = postService.findAllByUsers_DeletedFalseAndStatusRejected();
        model.addAttribute("totalPending", postService.findAllPostPending().size());
        model.addAttribute("approved", postService.countApproved());
        model.addAttribute("rejected", postService.countRejected());
        model.addAttribute("posts", rejectedPosts);
        model.addAttribute("rejectedTab", true);
        return "AdminDashboard/moderateNewBlogs";
    }

    @RequestMapping("/view/{id}")
    public String view(@PathVariable("id") int postID){
        return "redirect:/PostDetail/" + postID;
    }

    @RequestMapping("/approve/{id}")
    public String approve(@PathVariable("id") int postID){
        Posts post = postService.findPostByPostID(postID);
        post.setPublishedAt(new Timestamp(new Date().getTime()));
        post.setStatus("Approved");
        post.setPublished(true);
        postService.savePost(post);
        return "redirect:/admin/moderate/moderateNewBlogs";
    }

    @RequestMapping("/reject/{id}")
    public String reject(@PathVariable("id") int postID){
        Posts post = postService.findPostByPostID(postID);
        post.setStatus("Rejected");
        post.setReasonRejected("dwadawdawdad");
        post.setPublished(false);
        post.setUpdatedAt(new Timestamp(new Date().getTime()));
        postService.savePost(post);
        return "redirect:/admin/moderate/moderateNewBlogs";
    }

    @RequestMapping("/restore/{id}")
    public String restore(@PathVariable("id") int postID){
        Posts post = postService.findPostByPostID(postID);
        post.setStatus("Pending");
        post.setPublished(false);
        post.setReasonRejected(null);
        postService.savePost(post);
        return "redirect:/admin/moderate/moderateRejectedBlogs";
    }

    @RequestMapping("/search")
    public String search(@RequestParam("search") String searchQuery, Model model){
        List<Posts> allPostPending = postService.findAllPostPending();

        List<Posts> filteredPosts = allPostPending.stream()
                .filter(post ->
                    post.getTitle().toLowerCase().contains(searchQuery.toLowerCase()) ||
                    post.getContent().toLowerCase().contains(searchQuery.toLowerCase()) ||
                    post.getUsers().getFullName().toLowerCase().contains(searchQuery.toLowerCase()) ||
                    post.getUsers().getUsername().toLowerCase().contains(searchQuery.toLowerCase())
                )
                .collect(java.util.stream.Collectors.toList());

        model.addAttribute("posts", filteredPosts);
        model.addAttribute("totalPending", allPostPending.size());
        model.addAttribute("approved", postService.countApproved());
        model.addAttribute("rejected", postService.countRejected());
        model.addAttribute("searchQuery", searchQuery);
        return "AdminDashboard/moderateNewBlogs";
    }
}
