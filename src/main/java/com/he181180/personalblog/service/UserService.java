package com.he181180.personalblog.service;

import com.he181180.personalblog.entity.Users;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface UserService {
    public Optional<Users> findUserByUsername(String username);
    public Optional<Users> findUserByEmail(String email);
    public Users updateUser(Users user);
    public Users getCurrentUser();

    String saveAvatar(MultipartFile file, String username);

    Users findByUsername(String username);
    public Users saveUser(Users user);

    public void changeUserPassword(Users user, String password);

    void sendConfirmationCodeEmail(String email, String code);

    boolean checkPassword(Users user, String rawPassword);
    Optional<Users> findUserById(int id);
    List<Users> getAllUsers();
    List<Users> findUserByFullNameOrUserName(String name);
    void updateRole(int userID,String role);
    void delete(int userID);
    void recover(int userID);

    Users findUserByUserIDAndDeletedFalse(int userID);
    List<Users> findFollowerByUserID(int userID);
    Users findUserByUserNameAndDeletedFalse(String userName);
}