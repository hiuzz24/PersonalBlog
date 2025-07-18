package com.he181180.personalblog.service;

import com.he181180.personalblog.entity.Tags;

import java.util.List;

public interface TagService {
     List<Tags> getAllTags();
     List<Integer> findTagIDByPostID(int postID);
}
