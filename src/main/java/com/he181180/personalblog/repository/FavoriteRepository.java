package com.he181180.personalblog.repository;

import com.he181180.personalblog.entity.Favorites;
import com.he181180.personalblog.entity.Posts;
import com.he181180.personalblog.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorites, Long> {

    // Check if a post is favorited by a user
    boolean existsByUserAndPost(Users user, Posts post);

    // Find favorite by user and post
    Optional<Favorites> findByUserAndPost(Users user, Posts post);

    // Get all favorite posts by user, ordered by creation date (newest first)
    @Query("SELECT f FROM Favorites f JOIN FETCH f.post p WHERE f.user = :user AND p.published = true ORDER BY f.createdAt DESC")
    List<Favorites> findByUserOrderByCreatedAtDesc(@Param("user") Users user);

    // Count favorites for a user
    long countByUser(Users user);

    // Delete favorite by user and post
    void deleteByUserAndPost(Users user, Posts post);
}
