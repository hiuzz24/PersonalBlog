package com.he181180.personalblog.service.impl;

import com.he181180.personalblog.entity.Comments;
import com.he181180.personalblog.repository.CommentRepository;
import com.he181180.personalblog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentRepository commentRepository;

    @Override
    public void saveComment(com.he181180.personalblog.entity.Comments comment) {
        commentRepository.save(comment);
    }

    @Override
    public int countCommentByPostId(int postID) {
        return commentRepository.countCommentByPostID(postID);
    }

    @Override
    public List<Comments> getCommentsByPostId(int postId) {
        // Get all comments for the post
        List<Comments> allComments = commentRepository.getCommentsByPostIDHierarchical(postId);

        if (allComments.isEmpty()) {
            return new ArrayList<>();
        }

        List<Comments> rootComments = new ArrayList<>();
        Map<Integer, Comments> commentMap = new HashMap<>();

        // First pass: create a map of all comments and initialize children lists
        for (Comments comment : allComments) {
            comment.setChildren(new ArrayList<>()); // Initialize children list
            commentMap.put(comment.getCommentId(), comment);
        }

        // Second pass: build parent-child relationships
        for (Comments comment : allComments) {
            if (comment.getParentComment() == null) {
                // Root comment (no parent)
                rootComments.add(comment);
            } else {
                // Child comment - add to parent's children list
                Comments parent = commentMap.get(comment.getParentComment().getCommentId());
                if (parent != null) {
                    parent.getChildren().add(comment);
                }
            }
        }

        return rootComments;
    }
}
