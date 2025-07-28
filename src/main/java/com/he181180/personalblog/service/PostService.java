package com.he181180.personalblog.service;

import com.he181180.personalblog.entity.Posts;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PostService {
    List<Posts> getAll();
    List<Posts> searchPostByTitleAndContent(String search);
    List<Posts> findTop5RecentPosts();
    List<Posts> findPostsByTagID(int tagID);
    List<Posts> getPaginatedPosts(int page, int size);
    int getTotalPostCount();
    void savePost(Posts post);
    Optional<Posts> getPostByID(int postID);
    Posts findPostByPostID(int postID);
    List<Posts> getPostByUserID(int userID);
    void deletePost(int postID);
    void recoverPost(int postID);
    List<Posts> getAllPosts();
    List<Posts> findAllPostPending();
    String handleImageUrl(String imageUrl, MultipartFile fileImage) throws IOException;
    long countApprovedToday();
    long countRejectedToday();
    Long countPendingPost();
    Map<String,Integer> getPostCountByTag();
    Map<String,Integer> top5Author();
    Map<String,Integer> postPerStatus();
    Map<String,Integer> postPerMonth(int year);
    List<Posts> findAllByUsers_DeletedFalseAndStatusRejected();
}