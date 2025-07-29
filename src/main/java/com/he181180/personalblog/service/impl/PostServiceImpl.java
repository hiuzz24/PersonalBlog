package com.he181180.personalblog.service.impl;

import com.he181180.personalblog.entity.Posts;
import com.he181180.personalblog.repository.PostRepository;
import com.he181180.personalblog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
        return postRepository.findTop5ByPublishedTrueAndUsers_DeletedFalseOrderByPublishedAtDesc();
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
        Posts post = postRepository.findPostByPostID(postID);
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
        Posts post = postRepository.findPostByIDs(postID);
        if (post != null) {
            post.setPublished(false);
            postRepository.save(post);
        }
    }

    @Override
    public void recoverPost(int postID) {
        Posts post = postRepository.findPostByIDs(postID);
        if (post != null) {
            post.setPublished(true);
            postRepository.save(post);
        }
    }

    @Override
    public List<Posts> getAllPosts() {
        return postRepository.findAllByUsers_DeletedFalseAndStatusApproved();
    }

    @Override
    public List<Posts> findAllPostPending() {
        return postRepository.findAllPostPending();
    }

    @Override
    public String handleImageUrl(String imageUrl, MultipartFile fileImage) throws IOException {
        if(imageUrl != null && !imageUrl.isEmpty()){
            return imageUrl;
        }
        if(!fileImage.isEmpty()){
            // Use project's static/img directory instead of external path
            String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/img/";
            String fileName = UUID.randomUUID() + "_" + fileImage.getOriginalFilename();
            Files.createDirectories(Path.of(uploadDir));
            Path path = Path.of(uploadDir + fileName);
            Files.copy(fileImage.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            return "/img/" + fileName;
        }
        return null;
    }

    @Override
    public long countApprovedToday() {
        return postRepository.countApprovedToday();
    }

    @Override
    public long countRejectedToday() {
        return postRepository.countRejectedToday();
    }

}
