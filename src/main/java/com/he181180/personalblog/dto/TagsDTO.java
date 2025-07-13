package com.he181180.personalblog.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TagsDTO {
    private int tagID;
    private String tagName;
    private List<PostsDTO> posts;
}
