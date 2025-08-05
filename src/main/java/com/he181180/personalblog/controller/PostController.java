package com.he181180.personalblog.controller;

import com.he181180.personalblog.DTO.CommentReplyDTO;
import com.he181180.personalblog.entity.Comments;
import com.he181180.personalblog.entity.Notification;
import com.he181180.personalblog.entity.Posts;
import com.he181180.personalblog.entity.Users;
import com.he181180.personalblog.service.*;
import com.he181180.personalblog.service.TagService;
import com.he181180.personalblog.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.apache.bcel.generic.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Autowired
    private TagService tagService;

    @Autowired
    private CurrentUserService currentUserService;
    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private FollowService followService;

    @Autowired
    private NotificationService notificationService;


    @GetMapping("/explore")
    public String explore(@RequestParam(defaultValue = "1") int page, Model model){
        int pageSize = 6;

        List<Posts> posts = postService.getPaginatedPosts(page, pageSize);
        int totalPosts = postService.getTotalPostCount();
        int totalPages = (int) Math.ceil((double) totalPosts / pageSize);

        model.addAttribute("allPost", posts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        return "explore";
    }

    @RequestMapping("/search")
    public String searchByTitleAndContent(@RequestParam("search") String search, Model model){
        List<Posts> posts = postService.searchPostByTitleAndContent(search);
        model.addAttribute("allPost", posts);
        model.addAttribute("currentPage", 1);
        model.addAttribute("totalPages", 1);

        model.addAttribute("searchKeyword", search);
        return "explore";
    }

    @RequestMapping("/tags/{tagID}")
    public String getPostsByTag(@PathVariable("tagID") int tagID, Model model){
        List<Posts> posts = postService.findPostsByTagID(tagID);
        model.addAttribute("allPost", posts);
        model.addAttribute("currentPage", 1);
        model.addAttribute("totalPages", 1);
        return "explore";
    }

    @GetMapping("/PostDetail/{postID}")
    public String postDetail(@PathVariable("postID") int postID
            , Model model, Authentication authentication) {
        Posts posts = postService.findPostByPostID(postID);
        List<Integer> tagIDs = tagService.findTagIDByPostID(postID);
        List<Posts> postsList = tagIDs.stream().flatMap(tagID -> postService.findPostsByTagID(tagID).stream())
                .filter(p -> p.getPostID() != postID)
                .distinct()
                .collect(Collectors.toList());

        Collections.shuffle(postsList);
        List<Posts> randomFive = postsList.stream()
                .limit(5).collect(Collectors.toList());
        model.addAttribute("relatedPosts",randomFive);
        model.addAttribute("postDetail",posts);
        model.addAttribute("post", posts);
        int countComment = commentService.countCommentByPostId(postID);
        List<Comments> comments = commentService.getCommentsByPostId(postID);

        // Initialize CommentReplyDTO with post information
        CommentReplyDTO commentReplyDTO = CommentReplyDTO.builder()
                .post(posts)
                .build();
        model.addAttribute("commentReplyDTO", commentReplyDTO);

        // Check if post is favorited by current user using CurrentUserService
        boolean isFavorited = false;
        Users currentUser = null ;
        if (authentication != null) {
            try {
                currentUser = currentUserService.getCurrentUser(authentication);
                if (currentUser != null) {
                    isFavorited = favoriteService.isPostFavorited(currentUser, posts);
                }
            } catch (Exception e) {
                // Handle exception silently
            }
        }

        boolean isFollowing = followService.isFollowing(posts.getUsers().getUserID() , authentication);
        model.addAttribute("isFavorited", isFavorited);
        model.addAttribute("isFollowing", isFollowing);
        model.addAttribute("comments", comments);
        model.addAttribute("countComment", countComment);
        return "postDetail";
    }

    @PostMapping("/uploads/img")
    @ResponseBody
    public Map<String,String> uploadImageForCkeditor(@RequestParam("upload")MultipartFile upload) throws IOException {
        return postService.uploadImageForCkeditor(upload);
    }

}
