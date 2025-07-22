package com.he181180.personalblog.repository;

import com.he181180.personalblog.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users,Integer> {

    Optional<Users> findByUsername(String username);
    Optional<Users> findByEmail(String email);
}
