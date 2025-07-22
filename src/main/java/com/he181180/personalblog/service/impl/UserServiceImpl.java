package com.he181180.personalblog.service.impl;

import com.he181180.personalblog.entity.Users;
import com.he181180.personalblog.repository.UserRepository;
import com.he181180.personalblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Optional<Users> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Users updateUser(Users user) {
        return userRepository.save(user);
    }

    @Override
    public Users getCurrentUser() {
        return null;
    }

    @Override
    public Optional<Users> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    @Override
    public String saveAvatar(MultipartFile file, String username) {
        // Implement the logic to save the avatar file and return the URL
        // This is a placeholder implementation
        return "/img/default-avatar.png"; // Replace with actual file saving logic
    }
    @Override
    public Users findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
    @Override
    public Users saveUser(Users user) {
        return userRepository.save(user);
    }
    @Override
    public void changeUserPassword(Users user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }
}