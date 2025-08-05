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

    @Query("SELECT COUNT(p) FROM Posts p WHERE p.status = 'Approved'")
    long countApproved();

    @Query("SELECT COUNT(p) FROM Posts p WHERE p.status = 'Rejected'")
    long countRejected();

    @Query("SELECT p FROM Posts p WHERE p.users.userID = :userID " +
            "AND p.users.deleted = false " +
            "and p.published = true " +
            "order by p.published desc")
    Page<Posts> findAllPostsByUserIDPagination(@Param("userID") int userID, Pageable pageable);

    @Query("SELECT p FROM Posts p WHERE p.users.userID = :userID " +
            "AND p.users.deleted = false " +
            "order by p.published desc")
    List<Posts> findAllPostsByUserID(@Param("userID") int userID);

    @Query("SELECT COUNT(p) FROM Posts p WHERE p.status = 'Pending'")
    Long countPendingPosts();


    @Query("select t.tagName,count(p) from Posts p " +
            "join p.tags t where " +
            "p.status = 'Approved' and p.published = true " +
            "group by t.tagName")
    List<Object[]> getPostCountByTag();

    @Query("select p.users.fullName,count(p) from Posts p " +
            "where p.status = 'Approved' " +
            "and p.published = true " +
            "group by p.users.fullName " +
            "order by count (p) desc")
    List<Object[]> top5Author(Pageable pageable);

    @Query("select p.status,count(p) from Posts p " +
            "group by p.status")
    List<Object[]> postPerStatus();

    @Query("select  month (p.publishedAt),count(p) from Posts p " +
            "where year(p.publishedAt) = :year and p.published = true and p.status = 'Approved' " +
            "group by month(p.publishedAt) " +
            "order by month(p.publishedAt)")
    List<Object[]> postPerMonth(@Param("year") int year);

    @Query("select p from Posts p join p.users u where u.deleted = false and p.status = 'Rejected'")
    List<Posts> findAllByUsers_DeletedFalseAndStatusRejected();
}
