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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
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
    public String getPostsByUserId(Authentication authentication, Model model) {
        Users user = null;
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof org.springframework.security.oauth2.core.user.OAuth2User oauth2User) {
                String email = oauth2User.getAttribute("email");
                user = userService.findUserByEmail(email).orElse(null);
            } else {
                String username = authentication.getName();
                user = userService.findUserByUsername(username).orElse(null);
            }
        }
        if (user != null) {
            List<Posts> userPosts = postService.getPostByUserID(user.getUserID());
            model.addAttribute("posts", userPosts);
            model.addAttribute("user", user);
            model.addAttribute("activeTab", "posts");
        }
        return "UserDashboard/SelfBlogManagement";
    }

    // Hiển thị form tạo bài viết mới
    @GetMapping("/create")
    public String createPost(Model model) {
        model.addAttribute("post", new Posts());
        model.addAttribute("formAction", "/blog/create");
        return "BlogManagement/blogCreation";
    }

    // Lưu bài viết mới
    @PostMapping("/create")
    public String savePost(@RequestParam String title,
                           @RequestParam String content,
                           @RequestParam List<Integer> tagID,
                           @RequestParam String body,
                           @RequestParam String imageUrl,
                           @RequestParam(required = false) MultipartFile fileImage,
                           Authentication authentication,
                           Model model) {

        List<Tags> tags = tagService.findTagsByTagID(tagID);

        Users user = null;
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof org.springframework.security.oauth2.core.user.OAuth2User oauth2User) {
                String email = oauth2User.getAttribute("email");
                user = userService.findUserByEmail(email).orElse(null);
            } else {
                String username = authentication.getName();
                user = userService.findUserByUsername(username).orElse(null);
            }
        }

        if (user != null) {
            Posts post = new Posts();
            post.setTitle(title);
            post.setContent(content);
            post.setImageUrl(imageUrl);
            post.setTags(tags);
            post.setBody(body);
            post.setPublishedAt(new Timestamp(new Date().getTime()));
            post.setUsers(user);
            post.setPublished(true);
            post.setUpdatedAt(new Timestamp(new Date().getTime()));
            try {
                String finalImage = postService.handleImageUrl(imageUrl, fileImage);
                post.setImageUrl(finalImage);
            } catch (Exception e) {
                model.addAttribute("error", "Lỗi khi xử lý ảnh: " + e.getMessage());
                return "redirect:/blog/create";
            }
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
        List<Integer> selectedTagID = post.getTags().stream()
                .map(Tags::getTagID)
                .collect(Collectors.toList());

        model.addAttribute("selectedTagID", selectedTagID);
        model.addAttribute("post", post);
        model.addAttribute("formAction", "/blog/saveUpdate");
        return "BlogManagement/blogCreation";
    }

    @RequestMapping("/saveUpdate")
    public String saveUpdate(@RequestParam int postID,
                             @RequestParam String title,
                             @RequestParam List<Integer> tagID,
                             @RequestParam String content,
                             @RequestParam String body,
                             @RequestParam(required = false) String imageUrl,
                             @RequestParam(value = "fileImage", required = false) MultipartFile fileImage,
                             Authentication authentication) throws IOException {
        String username = authentication.getName();
        Optional<Users> user = userService.findUserByUsername(username);
        List<Tags> tags = tagService.findTagsByTagID(tagID);

        if (user.isPresent()) {
            Posts post = postService.findPostByPostID(postID);
            post.setTitle(title);
            post.setContent(content);
            post.setImageUrl(imageUrl);
            post.setTags(tags);
            post.setBody(body);
            post.setPublishedAt(post.getPublishedAt());
            post.setUsers(user.get());
            post.setPublished(true);
            post.setUpdatedAt(new Timestamp(new Date().getTime()));
            try {
                String finalImage = postService.handleImageUrl(imageUrl, fileImage);
                post.setImageUrl(finalImage);
            } catch (Exception e) {
                // Optionally, you can add a model attribute for error and redirect
                return "redirect:/blog/edit/" + postID + "?error=img";
            }
            postService.savePost(post);
            return "redirect:/blog";
        }
        return "redirect:/blog/edit/" + postID + "?error=user";
    }

    // Xóa bài viết
    @RequestMapping("/delete/{postID}")
    public String deletePost(@PathVariable int postID) {
        postService.deletePost(postID);
        return "redirect:/blog";
    }
}


