package com.he181180.personalblog.mapper;

import com.he181180.personalblog.dto.CommentsDTO;
import com.he181180.personalblog.entity.Comments;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface CommentMapper {

    @Mapping(target = "parentCommentID", source = "parentComment.commentID")
    CommentsDTO toDTO(Comments comment);

    @Mapping(target = "parentComment", expression = "java(mapParentById(dto.getParentCommentID()))")
    @Mapping(target = "posts", ignore = true)
    @Mapping(target = "isDeleted", constant = "false")
    Comments toEntity(CommentsDTO dto);

    default Comments mapParentById(Integer id) {
        if (id == null) return null;
        return Comments.builder().commentID(id).build();
    }
}
