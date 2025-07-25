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
    Optional<Users> findById(int id);
    Optional<Users> findByUsernameAndDeletedFalse(String username);
    Optional<Users> findByEmailAndDeletedFalse(String email);

    @Query("SELECT u FROM Users u " +
            "WHERE (LOWER(u.username) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND u.deleted = false")
    List<Users> findUsersByUsernameOrFullName(@Param("name") String name);
    List<Users> findAllByDeletedFalse();
    Users findUsersByUserIDAndDeletedFalse(int userID);
}
