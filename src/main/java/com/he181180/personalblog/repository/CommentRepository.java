package com.he181180.personalblog.repository;

import com.he181180.personalblog.entity.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CommentRepository extends JpaRepository<Comments,Integer> {
   @Query(value = "select count(*) from comments where post_id = :postID",nativeQuery = true)
    int countCommentByPostID(int postID);

    @Query(value = "select * from comments where post_id = :postID",nativeQuery = true)
    List<Comments> findCommentsByPostID (int postID);
}
