package com.he181180.personalblog.controller;

import com.he181180.personalblog.entity.Notification;
import com.he181180.personalblog.entity.Posts;
import com.he181180.personalblog.entity.Tags;
import com.he181180.personalblog.entity.Users;
import com.he181180.personalblog.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ControllerAdvice
public class GlobalModelAttribute {

    @Autowired
    private PostService postService;

    @Autowired
    private TagService tagService;

    @Autowired
    private CurrentUserService currentUserService;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @ModelAttribute("recentPosts")
    public List<Posts> recentPosts(){
        return postService.findTop5RecentPosts();
    }

    @ModelAttribute("allTags")
    public List<Tags> allTags(){
        return tagService.getAllTags();
    }

    @ModelAttribute("totalPost")
    public Integer totalPost(){
        return  postService.getAllPosts().size();
    }

    @ModelAttribute("totalUser")
    public Integer totalUser(){
        return userService.getAllUsers().size();
    }

    @ModelAttribute("pendingPost")
    public Long totalPendingPost(){
        return postService.countPendingPost();
    }

    @ModelAttribute("user")
    public Users currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return currentUserService.getCurrentUser(authentication);
    }

    @ModelAttribute("notification")
    public List<Notification> allNotificationByUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return Collections.emptyList();
        }
        String userName = authentication.getName();
        Users user = userService.findUserByUserNameAndDeletedFalse(userName);
        if (user == null) {
            return Collections.emptyList();
        }
        return notificationService.findNotificationsByUserID(user.getUserID());
    }
}
