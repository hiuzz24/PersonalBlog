package com.he181180.personalblog.service;

import com.he181180.personalblog.entity.Tags;

import java.util.List;
import java.util.Set;

public interface TagService {
     List<Tags> getAllTags();
     List<Integer> findTagIDByPostID(int postID);
     Set<Tags> findTagsByTagID(List<Integer> tagID);
}
