package com.he181180.personalblog.config;

import com.he181180.personalblog.entity.Users;
import com.he181180.personalblog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PasswordMigrationRunner implements CommandLineRunner {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        List<Users> allUsers = userRepo.findAll();
        for (Users user : allUsers) {
            String password = user.getPassword();
            if (password != null && !password.startsWith("$2a$")) {
                user.setPassword(passwordEncoder.encode(password));
                userRepo.save(user);
            }
        }
    }

}

