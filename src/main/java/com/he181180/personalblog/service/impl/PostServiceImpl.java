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
        return postRepository.findAll()
                .stream()
                .filter(Posts::isPublished)
                .collect(Collectors.toList());
    }

    @Override
    public List<Posts> searchPostByTitleAndContent(String search) {
        return postRepository.findByContentAndTitle(search);
    }

    @Override
    public List<Posts> findTop5RecentPosts() {
        return postRepository.findTop5ByIsPublishedTrueOrderByPublishedAtDesc();
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

    @Override
    public int getTotalPostCount() {
        return (int) postRepository.findAll()
                .stream()
                .filter(Posts::isPublished)
                .count();
    }

    @Override
    public Posts findPostByPostID(int postID) {
        Posts post = postRepository.findPostsByPostID(postID);
        return (post != null && post.isPublished()) ? post : null;
    }

    @Override
    public List<Posts> findPostsByTagID(int tagID) {
        return postRepository.findPostsByTagID(tagID);
    }

    @Override
    public Optional<Posts> getPostByID(int postID) {
        Optional<Posts> post = postRepository.findById(postID);
        return post.filter(Posts::isPublished);
    }

    @Override
    public List<Posts> getPostByUserID(int userID) {
        return postRepository.findAll().stream()
                .filter(post -> post.isPublished() && post.getUsers().getUserID() == userID)
                .collect(Collectors.toList());
    }

    @Override
    public void deletePost(int postID) {
        Posts post = postRepository.findPostsByPostID(postID);
        if (post != null) {
            post.setPublished(false);
            postRepository.save(post);
        }
    }
}
