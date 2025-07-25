package com.he181180.personalblog.service.impl;

import com.he181180.personalblog.entity.Users;
import com.he181180.personalblog.repository.UserRepository;
import com.he181180.personalblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JavaMailSender mailSender;

    @Override
    public Optional<Users> findUserByUsername(String username) {
        return userRepository.findByUsernameAndDeletedFalse(username);
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
    public List<Users> getAllUsers() {
        return userRepository.findAll();
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
    public List<Users> findUserByFullNameOrUserName(String name) {
        return userRepository.findUsersByUsernameOrFullName(name);
    }

    @Override
    public void updateRole(int userID,String role) {
        Users user = userRepository.findUsersByUserID(userID);
        user.setRole(role);
        userRepository.save(user);
    }
    @Override
    public void delete(int userID) {
        Users users = userRepository.findUsersByUserID(userID);
        users.setDeleted(true);
        userRepository.save(users);
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
    @Override
    public void sendConfirmationCodeEmail(String email, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your Confirmation Code");
        message.setText("Your confirmation code is: " + code);
        mailSender.send(message);
    }
    @Override
    public boolean checkPassword(Users user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }
    @Override
    public Optional<Users> findUserById(int id) {
        return userRepository.findById(id);
    }
    @Override
    public void recover(int userID) {
        Users users = userRepository.findUsersByUserID(userID);
        users.setDeleted(false);
        userRepository.save(users);
    }
}