package com.he181180.personalblog.controller;

import com.he181180.personalblog.entity.Posts;
import com.he181180.personalblog.entity.Users;
import com.he181180.personalblog.service.PostService;
import com.he181180.personalblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/author")
public class AuthorProfileController {
    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;

    @GetMapping("ProfileUser/{userID}")
    public String getProfileByUserID(@PathVariable("userID") int userID,
                                     @RequestParam(defaultValue = "1") int page,
                                     Model model){
        int size = 5;
        Users user = userService.findUserByUserIDAndDeletedFalse(userID);
        Page<Posts> totalPost = postService.getPostByUserIDPagination(userID,page-1,size);
        model.addAttribute("userProfile",user);
        model.addAttribute("totalPostSize",totalPost.getTotalElements());
        model.addAttribute("totalPost",totalPost);
        model.addAttribute("currentPage",page);
        model.addAttribute("totalPages",totalPost.getTotalPages());
        return "AuthorProfile/author-profile";
    }
}
