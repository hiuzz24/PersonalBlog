package com.he181180.personalblog.mapper;

import com.he181180.personalblog.dto.PostsDTO;
import com.he181180.personalblog.entity.Posts;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = {UserMapper.class})
public interface PostMapper {
    @Mapping(target = "users", source = "users")
    @Mapping(target = "tags", source = "tags")
    PostsDTO toDTO(Posts posts);

    @Mapping(target = "users", source = "users")
    @Mapping(target = "tags", source = "tags")
    @Mapping(target = "comments",ignore = true)
    Posts toEntity(PostsDTO postsDTO);


}
