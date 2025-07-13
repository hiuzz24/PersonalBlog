package com.he181180.personalblog.mapper;

import com.he181180.personalblog.dto.TagsDTO;
import com.he181180.personalblog.entity.Tags;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

@Mapper(componentModel = "spring")
public abstract class TagMapper {

    @Autowired
    @Lazy
    protected PostMapper postMapper;

    public TagsDTO toDTO(Tags tag) {
        if (tag == null) return null;

        return TagsDTO.builder()
                .tagID(tag.getTagID())
                .tagName(tag.getTagName())
                .posts(
                        tag.getPosts() == null ? null :
                                tag.getPosts().stream().map(postMapper::toDTO).toList()
                )
                .build();
    }

    public Tags toEntity(TagsDTO dto) {
        if (dto == null) return null;

        return Tags.builder()
                .tagID(dto.getTagID())
                .tagName(dto.getTagName())
                .build();
    }
}

