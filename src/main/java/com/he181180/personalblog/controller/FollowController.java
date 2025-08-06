package com.he181180.personalblog.controller;

import com.he181180.personalblog.entity.Users;
import com.he181180.personalblog.service.CurrentUserService;
import com.he181180.personalblog.service.FollowService;
import com.he181180.personalblog.service.NotificationService;
import com.he181180.personalblog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/follows")
@RequiredArgsConstructor
public class FollowController {

    @Autowired
    private final FollowService followService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;

    @Autowired
    private CurrentUserService currentUserService;

    @PostMapping("/follow/{userId}")
    public ResponseEntity<?> followUser(@PathVariable("userId") int userId, Authentication authentication) {
        System.out.println("Following user with ID: " + userId);
        followService.followUser(userId, authentication);
        Users currentUser = currentUserService.getCurrentUser(authentication);
        Users user = userService.findUserByUserIDAndDeletedFalse(userId);
        notificationService.createNotification(user,currentUser,"follow",null);
        return ResponseEntity.ok(Map.of("success", true, "message", "Followed successfully"));
    }

    @DeleteMapping("/unfollow/{userId}")
    public ResponseEntity<?> unfollowUser(@PathVariable("userId") int userId, Authentication authentication) {
        followService.unfollowUser(userId, authentication);
        return ResponseEntity.ok(Map.of("success", true, "message", "Unfollowed successfully"));
    }


    @GetMapping("/followers")
    public ResponseEntity<?> getFollowers(Authentication authentication) {
        try {
            Set<Users> followers = followService.getFollowers(authentication);
            Set<Map<String, Object>> followerData = followers.stream()
                    .map(user -> {
                        Map<String, Object> userMap = new HashMap<>();
                        userMap.put("userId", user.getUserID());
                        userMap.put("username", user.getUsername());
                        userMap.put("fullName", user.getFullName() != null ? user.getFullName() : "");
                        userMap.put("avatarUrl", user.getAvatarUrl() != null ? user.getAvatarUrl() : "");
                        return userMap;
                    })
                    .collect(Collectors.toSet());
            return ResponseEntity.ok(Map.of("success", true, "followers", followerData));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/following")
    public ResponseEntity<?> getFollowing(Authentication authentication) {
        try {
            Set<Map<String, Object>> followingData = followService.getFollowing(authentication)
                    .stream()
                    .map(user -> {
                        Map<String, Object> userMap = new HashMap<>();
                        userMap.put("userId", user.getUserID());
                        userMap.put("username", user.getUsername());
                        userMap.put("fullName", user.getFullName() != null ? user.getFullName() : "");
                        userMap.put("avatarUrl", user.getAvatarUrl() != null ? user.getAvatarUrl() : "");
                        return userMap;
                    })
                    .collect(Collectors.toSet());
            return ResponseEntity.ok(Map.of("success", true, "following", followingData));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @GetMapping
    public String getFollows() {
        return "UserDashboard/Follows";
    }

}


