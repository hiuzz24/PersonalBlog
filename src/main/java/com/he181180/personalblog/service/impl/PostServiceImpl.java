package com.he181180.personalblog.service.impl;

import com.he181180.personalblog.entity.Posts;
import com.he181180.personalblog.repository.PostRepository;
import com.he181180.personalblog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepository postRepository;


    @Override
    public List<Posts> getAll() {
        List<Posts> posts = postRepository.findAll();
        return posts;
    }

    @Override
    public List<Posts> searchPostByTitleAndContent(String search) {
        return postRepository.findByContentAndTitle(search);
    }

    @Override
    public List<Posts> findTop5RecentPosts() {
        return postRepository.findTop5ByOrderByPublishedAtDesc();
    }
    @Override
    public List<Posts> getPaginatedPosts(int page, int size) {
        int start = (page - 1) * size;
        return postRepository.findPostsWithPagination(start, size);
    }

    @Override
    public void savePost(Posts post) {
        postRepository.save(post);
    }
    public int getTotalPostCount() {
        return (int) postRepository.count();
    }

    @Override
    public Posts findPostByPostID(int postID) {
        return postRepository.findPostsByPostID(postID);
    }

    @Override
    public List<Posts> findPostsByTagID(int tagID) {
        return postRepository.findPostsByTagID(tagID);
    }

    @Override
    public Optional<Posts> getPostByID(int userID) {
        return postRepository.findById(userID);
    }
}
