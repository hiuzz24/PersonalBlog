package com.he181180.personalblog.service;

import com.he181180.personalblog.entity.Posts;

import java.util.List;

public interface PostService {
    List<Posts> getAll();
    List<Posts> searchPostByTitleAndContent(String search);
    List<Posts> findTop5RecentPosts();
    List<Posts> findPostsByTagID(int tagID);
    List<Posts> getPaginatedPosts(int page, int size);
    int getTotalPostCount();
    Posts findPostByPostID(int postID);
}
