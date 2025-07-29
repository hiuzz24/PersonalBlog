package com.he181180.personalblog.repository;

import com.he181180.personalblog.entity.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Posts, Integer> {

    @Query("SELECT p FROM Posts p " +
            "WHERE p.published = true AND p.users.deleted = false AND " +
            "(LOWER(p.title) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(p.content) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<Posts> findByContentAndTitle(@Param("search") String search);

    List<Posts> findTop5ByPublishedTrueAndUsers_DeletedFalseOrderByPublishedAtDesc();

    @Query("SELECT p FROM Posts p " +
            "JOIN p.tags t " +
            "WHERE p.published = true AND p.users.deleted = false AND t.tagID = :tagID")
    List<Posts> findPostsByTagID(@Param("tagID") int tagID);

    @Query(value = """
        SELECT p.* FROM posts p
        JOIN users u ON p.user_id = u.user_id
        WHERE p.is_published = 1 AND u.is_deleted = 0
        LIMIT :size OFFSET :start
        """, nativeQuery = true)
    List<Posts> findPostsWithPagination(@Param("start") int start, @Param("size") int size);

    @Query("SELECT p FROM Posts p WHERE p.postID = :postID AND p.users.deleted = false")
    Posts findPostByIDs(@Param("postID") int postID);

    @Query("select p from Posts p " +
            "join p.users u " +
            "where u.deleted = false and p.status = 'Approved'")
    List<Posts> findAllByUsers_DeletedFalseAndStatusApproved();

    @Query("select p from Posts p " +
            "where p.status = 'Pending'")
    List<Posts> findAllPostPending();

    Posts findPostByPostID(int postID);

    @Query("SELECT COUNT(p) FROM Posts p WHERE p.status = 'Approved' AND DATE(p.publishedAt) = CURRENT_DATE")
    long countApprovedToday();

    @Query("SELECT COUNT(p) FROM Posts p WHERE p.status = 'Rejected' AND DATE(p.updatedAt) = CURRENT_DATE")
    long countRejectedToday();

}
