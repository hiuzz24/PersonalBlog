package com.he181180.personalblog.service.impl;

import com.he181180.personalblog.entity.Comments;
import com.he181180.personalblog.repository.CommentRepository;
import com.he181180.personalblog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return commentRepository.findCommentsByPostID(postId);
    }
}
