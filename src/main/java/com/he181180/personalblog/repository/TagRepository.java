package com.he181180.personalblog.repository;

import com.he181180.personalblog.entity.Tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TagRepository extends JpaRepository<Tags,Integer> {
    @Query("select t.tagID from Tags t " +
            "join t.posts p " +
            "where p.postID = :postID")
    List<Integer> findTagIDByPostID(@Param("postID") int postID);
}
