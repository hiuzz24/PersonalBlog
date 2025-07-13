package com.he181180.personalblog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostsDTO {
    private int postID;
    private String title;
    private String content;
    private boolean isPublished;
    private Date updatedAt;

    private UsersDTO users;
    private List<TagsDTO> tags;
}
