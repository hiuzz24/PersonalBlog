package com.he181180.personalblog.service.impl;

import com.he181180.personalblog.entity.Posts;
import com.he181180.personalblog.repository.PostRepository;
import com.he181180.personalblog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
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
        return postRepository.findTop5ByPublishedTrueAndUsers_DeletedFalseAndDeletedFalseOrderByPublishedAtDesc();
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
        return post;
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
    public Page<Posts> getPostByUserIDPagination(int userID, int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        return postRepository.findAllPostsByUserIDPagination(userID,pageable);
    }

    @Override
    public List<Posts> getPostByUserID(int userID) {
        return postRepository.findAllPostsByUserID(userID);
    }

    @Override
    public void deletePost(int postID) {
        Posts post = postRepository.findPostByIDs(postID);
        if (post != null) {
            post.setPublished(false);
            post.setDeleted(true);
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
        return postRepository.findAllByUsers_DeletedFalse();
    }

    @Override
    public List<Posts> findAllPostPending() {
        return postRepository.findAllPostPending();
    }

    @Override
    public String handleImageUrl(String imageUrl, MultipartFile fileImage) throws IOException {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            return imageUrl;
        }
        if (!fileImage.isEmpty()) {
            Map<String, String> url = new HashMap<>();

            String uploadDir = System.getProperty("user.dir") + "/uploads/img/";
            Files.createDirectories(Path.of(uploadDir));

            String fileName = UUID.randomUUID() + "_" + fileImage.getOriginalFilename();
            Path filePath = Path.of(uploadDir + fileName);

            Files.copy(fileImage.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return "/img/" + fileName;
        }
        return null;
    }

    @Override
    public long countApproved() {
        return postRepository.countApproved();
    }

    @Override
    public long countRejected() {
        return postRepository.countRejected();
    }

    @Override
    public Long countPendingPost() {
        return postRepository.countPendingPosts();
    }

    @Override
    public Map<String, Integer> getPostCountByTag() {
        List<Object[]> result = postRepository.getPostCountByTag();
        Map<String,Integer> map = new HashMap<>();
        for(Object[] row : result){
            String name = (String)row[0];
            Long value = (Long)row[1];
            map.put(name,value.intValue());
        }
        return map;
    }

    @Override
    public Map<String, Integer> top5Author() {
        Pageable top5 = PageRequest.of(0,5);
        List<Object[]> result = postRepository.top5Author(top5);
        Map<String,Integer> map = new HashMap<>();
        for(Object[] row : result){
            String name = (String)row[0];
            Long value = (Long)row[1];
            map.put(name,value.intValue());
        }
        return map;
    }

    @Override
    public Map<String, Integer> postPerStatus() {
        List<Object[]> result = postRepository.postPerStatus();
        Map<String,Integer> map  = new HashMap<>();
        for(Object[] row : result){
            String name = (String)row[0];
            Long value = (Long)row[1];
            map.put(name,value.intValue());
        }
        return map;
    }

    @Override
    public Map<String, Integer> postPerMonth(int year) {
        List<Object[]> result = postRepository.postPerMonth(year);
        Map<String,Integer> map = new HashMap<>();
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        for(String mon : months){
            map.put(mon,0);
        }

        for(Object[] row : result){
            Integer monthIndex = (Integer) row[0];
            Long value = (Long) row[1];
            map.put(months[monthIndex-1],value.intValue());
        }
        return map;
    }

    @Override
    public List<Posts> findAllByUsers_DeletedFalseAndStatusRejected() {
        return postRepository.findAllByUsers_DeletedFalseAndStatusRejected();
    }

    @Override
    public Map<String, String> uploadImageForCkeditor(MultipartFile upload) throws IOException {
        String uploadDir = System.getProperty("user.dir") + "/uploads/img/";
        Files.createDirectories(Path.of(uploadDir));

        String fileName = UUID.randomUUID() + "_" + upload.getOriginalFilename();
        Path filePath = Path.of(uploadDir + fileName);

        Files.copy(upload.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        Map<String,String> url = new HashMap<>();
        url.put("url", "/img/" + fileName);
        url.put("uploaded", "true");
        return url;
    }
}
