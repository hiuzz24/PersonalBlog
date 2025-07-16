package com.he181180.personalblog.repository;

import com.he181180.personalblog.entity.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Posts,Integer> {
    @Query("select p from Posts p " +
            "where lower(p.title) like lower(concat('%',:search,'%'))  " +
            "or lower(p.content) like lower(concat('%',:search,'%'))")
    List<Posts> findByContentAndTitle(@Param("search") String search);

    List<Posts> findTop5ByOrderByPublishedAtDesc();
}
