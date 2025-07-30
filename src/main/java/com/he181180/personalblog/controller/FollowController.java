package com.he181180.personalblog.controller;

import com.he181180.personalblog.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

        import java.util.Map;

@RestController
@RequestMapping("/api/follows")
@RequiredArgsConstructor
public class FollowController {

    @Autowired
    private final FollowService followService;

    @PostMapping("/follow/{userId}")
    public ResponseEntity<?> followUser(@PathVariable("userId") int userId, Authentication authentication) {
        System.out.println("Following user with ID: " + userId);
        followService.followUser(userId, authentication);
        return ResponseEntity.ok(Map.of("success", true, "message", "Followed successfully"));
    }

    @DeleteMapping("/unfollow/{userId}")
    public ResponseEntity<?> unfollowUser(@PathVariable("userId") int userId, Authentication authentication) {
        followService.unfollowUser(userId, authentication);
        return ResponseEntity.ok(Map.of("success", true, "message", "Unfollowed successfully"));
    }
}
