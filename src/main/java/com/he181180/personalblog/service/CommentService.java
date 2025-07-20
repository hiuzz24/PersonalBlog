package com.he181180.personalblog.service;

import com.he181180.personalblog.entity.Comments;

import java.util.List;

public interface CommentService {
    int countCommentByPostId(int postId);
    void saveComment(Comments comment);

    List<Comments> getCommentsByPostId(int postId);
}
