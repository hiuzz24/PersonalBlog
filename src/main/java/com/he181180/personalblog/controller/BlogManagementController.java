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
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/blog")
public class BlogManagementController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

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
        model.addAttribute("post", new Posts());
        return "BlogManagement/blogCreation";
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
    @PostMapping("/posts/edit/{id}")
    public String updatePost(@PathVariable int id,
                             @RequestParam String title,
                             @RequestParam String content,
                             @RequestParam String imageUrl,
                             Authentication authentication) {

        Optional<Posts> postOpt = postService.getPostByID(id);

        if (postOpt.isPresent()) {
            Posts post = postOpt.get();

            if (post.getUsers().getUsername().equals(authentication.getName())) {
                post.setTitle(title);
                post.setContent(content);
                post.setImageUrl(imageUrl);
                post.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

                postService.savePost(post);
            }
        }

        return "redirect:/dashboard/posts?updated";
    }

    // Xóa bài viết
    @PostMapping("/posts/delete/{id}")
    public String deletePost(@PathVariable int id, Authentication authentication) {
        Optional<Posts> postOpt = postService.getPostByID(id);

        if (postOpt.isPresent()) {
            Posts post = postOpt.get();

            if (post.getUsers().getUsername().equals(authentication.getName())) {
                postService.deletePost(id);
            }
        }

        return "redirect:/dashboard/posts?deleted";
    }

}