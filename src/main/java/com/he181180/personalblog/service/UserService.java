package com.he181180.personalblog.service;

import com.he181180.personalblog.entity.Users;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface UserService {
    public Optional<Users> findUserByUsername(String username);
    public Optional<Users> findUserByEmail(String email);
    public Users updateUser(Users user);
    public Users getCurrentUser();

    String saveAvatar(MultipartFile file, String username);

    Users findByUsername(String username);
    public Users saveUser(Users user);
    Optional<Users> findUserById(int id);
}