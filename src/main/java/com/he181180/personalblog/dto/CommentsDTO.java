package com.he181180.personalblog.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentsDTO {
    private int commentID;
    private String content;
    private UsersDTO users;
    private Integer parentCommentID;
    private List<CommentsDTO> replies;
}
