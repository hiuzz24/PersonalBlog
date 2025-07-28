package com.he181180.personalblog.controller;

import com.he181180.personalblog.entity.Favorites;
import com.he181180.personalblog.entity.Posts;
import com.he181180.personalblog.entity.Users;
import com.he181180.personalblog.service.CurrentUserService;
import com.he181180.personalblog.service.FavoriteService;
import com.he181180.personalblog.service.PostService;
import com.he181180.personalblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private PostService postService;

    @Autowired
    private CurrentUserService currentUserService;

    // Toggle favorite status via AJAX
    @PostMapping("/toggle/{postId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> toggleFavorite(@PathVariable int postId, Authentication authentication) {
        Map<String, Object> response = new HashMap<>();

        try {
            if (authentication == null) {
                response.put("success", false);
                response.put("message", "Please login to save posts");
                return ResponseEntity.ok(response);
            }

            // Get current user
            Users user = currentUserService.getCurrentUser(authentication);
            if (user == null) {
                response.put("success", false);
                response.put("message", "User not found");
                return ResponseEntity.ok(response);
            }

            // Get post
            Posts post = postService.findPostByPostID(postId);
            if (post == null) {
                response.put("success", false);
                response.put("message", "Post not found");
                return ResponseEntity.ok(response);
            }

            // Toggle favorite
            boolean isFavorited = favoriteService.toggleFavorite(user, post);

            response.put("success", true);
            response.put("isFavorited", isFavorited);
            response.put("message", isFavorited ? "Post saved to favorites" : "Post removed from favorites");

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    // Check if post is favorited
    @GetMapping("/check/{postId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkFavoriteStatus(@PathVariable int postId, Authentication authentication) {
        Map<String, Object> response = new HashMap<>();

        try {
            if (authentication == null) {
                response.put("isFavorited", false);
                return ResponseEntity.ok(response);
            }

            Users user = currentUserService.getCurrentUser(authentication);
            if (user == null) {
                response.put("isFavorited", false);
                return ResponseEntity.ok(response);
            }

            Posts post = postService.findPostByPostID(postId);
            if (post == null) {
                response.put("isFavorited", false);
                return ResponseEntity.ok(response);
            }

            boolean isFavorited = favoriteService.isPostFavorited(user, post);
            response.put("isFavorited", isFavorited);

        } catch (Exception e) {
            response.put("isFavorited", false);
        }

        return ResponseEntity.ok(response);
    }

    // Dashboard page for favorites
    @GetMapping("/dashboard")
    public String favoritesDashboard(Authentication authentication, Model model) {
        if (authentication == null) {
            return "redirect:/login";
        }
        Users user = currentUserService.getCurrentUser(authentication);
        if (user == null) {
            return "redirect:/login";
        }

        List<Favorites> favorites = favoriteService.getUserFavorites(user);
        long favoriteCount = favoriteService.countUserFavorites(user);

        model.addAttribute("favorites", favorites);
        model.addAttribute("favoriteCount", favoriteCount);
        model.addAttribute("user", user);

        return "UserDashboard/favorites";
    }

}
