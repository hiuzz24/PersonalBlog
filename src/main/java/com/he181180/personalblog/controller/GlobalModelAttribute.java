package com.he181180.personalblog.controller;

import com.he181180.personalblog.entity.Posts;
import com.he181180.personalblog.entity.Tags;
import com.he181180.personalblog.service.PostService;
import com.he181180.personalblog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class GlobalModelAttribute {
    @Autowired
    private PostService postService;

    @Autowired
    private TagService tagService;

    @ModelAttribute("recentPosts")
    public List<Posts> recentPosts(){
        return postService.findTop5RecentPosts();
    }
    @ModelAttribute("allTags")
    public List<Tags> allTags(){
        return tagService.getAllTags();
    }
}
