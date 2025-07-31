package com.he181180.personalblog.repository;

import com.he181180.personalblog.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users,Integer> {

    Optional<Users> findByUsername(String username);
    Optional<Users> findByEmail(String email);
    Optional<Users> findByUserIDAndDeletedFalse(int id);
    Optional<Users> findByUsernameAndDeletedFalse(String username);
    Optional<Users> findByEmailAndDeletedFalse(String email);


    @Query("SELECT u FROM Users u " +
            "WHERE (LOWER(u.username) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND u.deleted = false")
    List<Users> findUsersByUsernameOrFullName(@Param("name") String name);

    Users findUsersByUserID(int userID);

    List<Users> findAllByDeletedFalse();

    Users findUsersByUserIDAndDeletedFalse(int userID);

    @Query("SELECT u FROM Users u LEFT JOIN FETCH u.following WHERE u.userID = :userId")
    Optional<Users> findByIdWithFollowing(@Param("userId") int userId);

    @Query("SELECT u FROM Users u LEFT JOIN FETCH u.followers WHERE u.userID = :userId")
    Optional<Users> findByIdWithFollowers(@Param("userId") int userId);

    @Query(value = "SELECT COUNT(*) FROM follows WHERE following_id = :userID", nativeQuery = true)
    Integer totalFollower(@Param("userID") int userID);

    @Query(value = "SELECT COUNT(*) FROM follows WHERE follower_id = :userID", nativeQuery = true)
    Integer totalFollowing(@Param("userID") int userID);

    @Query(value = "SELECT u.* FROM users u " +
            "JOIN follows f ON u.user_id = f.follower_id " +
            "WHERE f.following_id = :userID", nativeQuery = true)
    List<Users> findFollowerByUserID(@Param("userID") int userID);

    Users findUsersByUsernameAndDeletedFalse(String userName);


}
