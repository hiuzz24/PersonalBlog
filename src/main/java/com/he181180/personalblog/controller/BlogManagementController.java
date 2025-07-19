package com.he181180.personalblog.controller;

import com.he181180.personalblog.entity.Posts;
import com.he181180.personalblog.entity.Tags;
import com.he181180.personalblog.entity.Users;
import com.he181180.personalblog.service.PostService;
import com.he181180.personalblog.service.TagService;
import com.he181180.personalblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/blog")
public class BlogManagementController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    private TagService tagService;

    @GetMapping
    public String getPostsByUserId(Authentication authentication,Model model) {
        String username = authentication.getName();
        Optional<Users> user = userService.findUserByUsername(username);
        if (user.isPresent()) {
            List<Posts> userPosts = postService.getPostByUserID(user.get().getUserID());
            model.addAttribute("posts", userPosts);
            model.addAttribute("user", user.get());
            model.addAttribute("activeTab", "posts");
        }
        return "UserDashboard/SelfBlogManagement";
    }

    // Hiển thị form tạo bài viết mới
    @GetMapping("/create")
    public String createPost(Model model) {
        model.addAttribute("post",new Posts());
        model.addAttribute("formAction","/blog/create");
        return "BlogManagement/blogCreation";
    }

    // Lưu bài viết mới
    @PostMapping("/create")
    public String savePost(@RequestParam String title,
                           @RequestParam String content,
                           @RequestParam List<Integer> tagID,
                           @RequestParam String body,
                           @RequestParam String imageUrl,
                           Authentication authentication,
                           Model model) {

        Set<Tags> tags = tagService.findTagsByTagID(tagID);

        String username = authentication.getName();
        Optional<Users> user = userService.findUserByUsername(username);

        if (user.isPresent()) {
            Posts post = new Posts();
            post.setTitle(title);
            post.setContent(content);
            post.setImageUrl(imageUrl);
            post.setTags(tags);
            post.setBody(body);
            post.setPublishedAt(new Timestamp(new Date().getTime()));
            post.setUsers(user.get());
            post.setPublished(true);
            post.setUpdatedAt(new Timestamp(new Date().getTime()));
            postService.savePost(post);
            return "redirect:/blog";
        }

        model.addAttribute("error", "Không thể tạo bài viết");
        return "redirect:/blog/create";
    }

    // Cập nhật bài viết
    @RequestMapping("/edit/{postID}")
    public String updatePost(@PathVariable("postID") int postID,
                             Model model) {
        Posts post = postService.findPostByPostID(postID);
        Set<Integer> selectedTagID = post.getTags().stream()
                .map(Tags::getTagID)
                .collect(Collectors.toSet());

        model.addAttribute("selectedTagID",selectedTagID);
        model.addAttribute("post",post);
        model.addAttribute("formAction","/blog/saveUpdate");
        return "BlogManagement/blogCreation";
    }

    @RequestMapping("/saveUpdate")
    public String saveUpdate(@RequestParam int postID,
                            @RequestParam String title,
                           @RequestParam String content,
                           @RequestParam String body,
                           @RequestParam String imageUrl,
                             Authentication authentication) {
        String username = authentication.getName();
        Optional<Users> user = userService.findUserByUsername(username);

        Posts post = postService.findPostByPostID(postID);
        post.setTitle(title);
        post.setContent(content);
        post.setImageUrl(imageUrl);
        post.setBody(body);
        post.setPublishedAt(post.getPublishedAt());
        post.setUsers(user.get());
        post.setPublished(true);
        post.setUpdatedAt(new Timestamp(new Date().getTime()));
        postService.savePost(post);
        return "redirect:/blog";
        }

    // Xóa bài viết
    @RequestMapping("/delete/{postID}")
    public String deletePost(@PathVariable int postID) {
        postService.deletePost(postID);
        return "redirect:/blog";
    }
    }


