package com.he181180.personalblog.service.impl;

import com.he181180.personalblog.entity.Tags;
import com.he181180.personalblog.repository.TagRepository;
import com.he181180.personalblog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagRepository tagRepository;

    @Override
    public List<Tags> getAllTags() {
        return tagRepository.findAll();
    }

    @Override
    public List<Integer> findTagIDByPostID(int postID) {
        return tagRepository.findTagIDByPostID(postID);
    }

    @Override
    public List<Tags> findTagsByTagID(List<Integer> tagID) {
        return tagRepository.findTagsByTagIDIn(tagID);
    }
}
