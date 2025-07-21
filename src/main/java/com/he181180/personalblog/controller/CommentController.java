package com.he181180.personalblog.controller;

import com.he181180.personalblog.DTO.CommentReplyDTO;
import com.he181180.personalblog.entity.Comments;
import com.he181180.personalblog.entity.Posts;
import com.he181180.personalblog.entity.Users;
import com.he181180.personalblog.service.CommentService;
import com.he181180.personalblog.service.PostService;
import com.he181180.personalblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public String addComment(@ModelAttribute CommentReplyDTO commentReplyDTO,
                           @RequestParam(value = "parentId", required = false) Integer parentId,
                           Authentication authentication,
                           RedirectAttributes redirectAttributes) {

        try {
            // Check authentication
            if (authentication == null || !authentication.isAuthenticated()) {
                redirectAttributes.addFlashAttribute("error", "You must be logged in to comment");
                return "redirect:/PostDetail/" + commentReplyDTO.getPost().getPostID();
            }
            // Support both standard and OAuth2 (Google) users
            Optional<Users> userOptional = Optional.empty();
            Object principal = authentication.getPrincipal();
            if (principal instanceof org.springframework.security.oauth2.core.user.OAuth2User oauth2User) {
                String email = oauth2User.getAttribute("email");
                userOptional = userService.findUserByEmail(email);
            } else {
                String username = authentication.getName();
                userOptional = userService.findUserByUsername(username);
            }

            if (userOptional.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "User not found");
                return "redirect:/PostDetail/" + commentReplyDTO.getPost().getPostID();
            }

            // Get post
            Posts post = postService.findPostByPostID(commentReplyDTO.getPost().getPostID());
            if (post == null) {
                redirectAttributes.addFlashAttribute("error", "Post not found");
                return "redirect:/PostDetail/" + commentReplyDTO.getPost().getPostID();
            }

            // Create comment from DTO
            Comments comment = Comments.builder()
                    .content(commentReplyDTO.getContent())
                    .post(post)
                    .user(userOptional.get())
                    .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                    .isDeleted(false)
                    .build();

            // Set parent comment if this is a reply
            if (parentId != null && parentId > 0) {
                Comments parentComment = new Comments();
                parentComment.setCommentId(parentId);
                comment.setParentComment(parentComment);
            }

            // Save comment
            commentService.saveComment(comment);
            redirectAttributes.addFlashAttribute("success", "Comment added successfully");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to add comment: " + e.getMessage());
        }

        return "redirect:/PostDetail/" + commentReplyDTO.getPost().getPostID();
    }

}
