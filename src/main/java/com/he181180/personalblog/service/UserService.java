package com.he181180.personalblog.service;

import com.he181180.personalblog.entity.Users;

import java.util.Optional;

public interface UserService {
    public Optional<Users> findUserByUsername(String username);
    public Users updateUser(Users user);
    public Users getCurrentUser();
}