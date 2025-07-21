package com.he181180.personalblog.DTO;

import com.he181180.personalblog.entity.Comments;
import com.he181180.personalblog.entity.Posts;
import com.he181180.personalblog.entity.Users;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentReplyDTO {

    private String content;

    private Posts post;

    private Users user;

    private Comments parentComment;
}
