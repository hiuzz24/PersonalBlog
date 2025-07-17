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
import java.util.Date;
import java.util.Optional;

@Controller
@RequestMapping("/blog")
public class BlogCreationController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;


    // Hiển thị form tạo bài viết mới
    @GetMapping("/create")
    public String createPost(Model model) {
        model.addAttribute("post", new Posts());
        return "blog/create-post";
    }

    // Lưu bài viết mới
    @PostMapping("/create")
    public String savePost(@RequestParam String title,
                           @RequestParam String content,
                           @RequestParam String imageUrl,
                           Authentication authentication,
                           Model model) {

        // Lấy user hiện tại
        String username = authentication.getName();
        Optional<Users> user = userService.findUserByUsername(username);

        if (user.isPresent()) {
            Posts post = new Posts();
            post.setTitle(title);
            post.setContent(content);
            post.setImageUrl(imageUrl);
            post.setUsers(user.get());
            post.setPublished(true);
            post.setUpdatedAt(new Timestamp(new Date().getTime()));
            postService.savePost(post);
            return "redirect:/blog/posts";
        }

        model.addAttribute("error", "Không thể tạo bài viết");
        return "blog/create-post";
    }
    // Cập nhật bài viết
    @PostMapping("/edit/{id}")
    public String updatePost(@PathVariable int id,
                             @RequestParam String title,
                             @RequestParam String content,
                             @RequestParam String imageUrl,
                             Authentication authentication) {

        Optional<Posts> postOpt = postService.getPostByID(id);
        if (postOpt.isPresent() && postOpt.get().getUsers().getUsername().equals(authentication.getName())) {
            Posts post = postOpt.get();
            post.setTitle(title);
            post.setContent(content);
            post.setImageUrl(imageUrl);
            post.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

            postService.savePost(post);
        }

        return "redirect:/blog/post/" + id;
    }

}
