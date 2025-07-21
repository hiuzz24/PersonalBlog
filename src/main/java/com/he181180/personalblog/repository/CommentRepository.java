package com.he181180.personalblog.repository;

import com.he181180.personalblog.entity.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comments,Integer> {
   @Query(value = "select count(*) from comments where post_id = :postID",nativeQuery = true)
    int countCommentByPostID(int postID);

    // Get all comments for a post ordered by creation time
    @Query(value = "SELECT * FROM comments WHERE post_id = :postID AND is_deleted = false ORDER BY created_at ASC", nativeQuery = true)
    List<Comments> getCommentsByPostIDHierarchical(@Param("postID") int postID);
    
    // Alternative method to get root comments only
    @Query(value = "SELECT * FROM comments WHERE post_id = :postID AND parent_comment_id IS NULL AND is_deleted = false ORDER BY created_at ASC", nativeQuery = true)
    List<Comments> getRootCommentsByPostID(@Param("postID") int postID);
    
    // Get child comments for a specific parent
    @Query(value = "SELECT * FROM comments WHERE parent_comment_id = :parentId AND is_deleted = false ORDER BY created_at ASC", nativeQuery = true)
    List<Comments> getChildCommentsByParentId(@Param("parentId") int parentId);
}
