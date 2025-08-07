package com.he181180.personalblog.repository;

import com.he181180.personalblog.entity.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface PostRepository extends JpaRepository<Posts, Integer> {

    @Query("SELECT p FROM Posts p " +
            "WHERE p.published = true AND p.deleted = false AND p.users.deleted = false AND " +
            "(LOWER(p.title) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(p.content) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<Posts> findByContentAndTitle(@Param("search") String search);

    List<Posts> findTop5ByPublishedTrueAndUsers_DeletedFalseAndDeletedFalseOrderByPublishedAtDesc();

    @Query("SELECT p FROM Posts p " +
            "JOIN p.tags t " +
            "WHERE p.published = true AND p.deleted = false AND p.users.deleted = false AND t.tagID = :tagID")
    List<Posts> findPostsByTagID(@Param("tagID") int tagID);

    @Query(value = """
        SELECT p.* FROM posts p
        JOIN users u ON p.user_id = u.user_id
        WHERE p.is_published = true AND p.is_deleted = false AND u.is_deleted = false
        LIMIT :size OFFSET :start
        """, nativeQuery = true)
    List<Posts> findPostsWithPagination(@Param("start") int start, @Param("size") int size);

    @Query("SELECT p FROM Posts p WHERE p.postID = :postID AND p.deleted = false ")
    Posts findPostByIDs(@Param("postID") int postID);

    @Query("SELECT p FROM Posts p " +
            "JOIN p.users u " +
            "WHERE u.deleted = false")
    List<Posts> findAllByUsers_DeletedFalse();

    @Query("SELECT p FROM Posts p WHERE p.status = 'Pending' AND p.deleted = false")
    List<Posts> findAllPostPending();

    @Query("SELECT p FROM Posts p WHERE p.status = 'Rejected'")
    List<Posts> findAllPostRejected();

    @Query("SELECT p FROM Posts p WHERE (p.status = 'Rejected' or p.status = 'Pending') AND p.deleted = false")
    List<Posts> findAllPostRejectedOrPending();

    @Query("select p from Posts p where p.postID = :postID and p.deleted = false ")
    Posts findPostByPostIDAndDeletedTrue(@Param("postID") int postID);

    @Query("select p from Posts p where p.postID = :postID and  p.deleted = false and ( p.status = 'Pending')")
    Posts findPostByPostIDAndDeletedFalse(@Param("postID") int postID);

    @Query("select p from Posts p where p.postID = :postID and  p.deleted = false ")
    Posts findPostByPostID(@Param("postID") int postID);

    @Query("SELECT COUNT(p) FROM Posts p WHERE p.status = 'Approved' ")
    long countApproved();

    @Query("SELECT COUNT(p) FROM Posts p WHERE p.status = 'Rejected' ")
    long countRejected();

    @Query("SELECT p FROM Posts p WHERE p.users.userID = :userID " +
            "AND p.users.deleted = false AND p.deleted = false AND p.published = true " +
            "ORDER BY p.published DESC")
    Page<Posts> findAllPostsByUserIDPagination(@Param("userID") int userID, Pageable pageable);

    @Query("SELECT p FROM Posts p WHERE p.users.userID = :userID " +
            "AND p.users.deleted = false AND p.deleted = false " +
            "ORDER BY p.published DESC")
    List<Posts> findAllPostsByUserID(@Param("userID") int userID);

    @Query("SELECT COUNT(p) FROM Posts p WHERE p.status = 'Pending' AND p.deleted = false")
    Long countPendingPosts();

    @Query("SELECT t.tagName, COUNT(p) FROM Posts p " +
            "JOIN p.tags t " +
            "WHERE p.status = 'Approved' AND p.published = true AND p.deleted = false " +
            "GROUP BY t.tagName")
    List<Object[]> getPostCountByTag();

    @Query("SELECT p.users.fullName, COUNT(p) FROM Posts p " +
            "WHERE p.status = 'Approved' AND p.published = true AND p.deleted = false " +
            "GROUP BY p.users.fullName " +
            "ORDER BY COUNT(p) DESC")
    List<Object[]> top5Author(Pageable pageable);

    @Query("SELECT p.status, COUNT(p) FROM Posts p WHERE p.deleted = false GROUP BY p.status")
    List<Object[]> postPerStatus();

    @Query("SELECT MONTH(p.publishedAt), COUNT(p) FROM Posts p " +
            "WHERE YEAR(p.publishedAt) = :year AND p.published = true AND p.status = 'Approved' AND p.deleted = false " +
            "GROUP BY MONTH(p.publishedAt) " +
            "ORDER BY MONTH(p.publishedAt)")
    List<Object[]> postPerMonth(@Param("year") int year);

    @Query("SELECT p FROM Posts p " +
            "JOIN p.users u " +
            "WHERE u.deleted = false AND p.deleted = true AND p.published = false AND p.status = 'Rejected'")
    List<Posts> findAllByUsers_DeletedFalseAndStatusRejected();


    @Query("SELECT p FROM Posts p WHERE " +
            "LOWER(p.title) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(p.users.fullName) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Posts> searchByTitleOrAuthor(@Param("search") String search);
}
