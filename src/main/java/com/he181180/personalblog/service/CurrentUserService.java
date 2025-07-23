package com.he181180.personalblog.service;

import com.he181180.personalblog.DTO.UserUpdateDTO;
import com.he181180.personalblog.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CurrentUserService {

    @Autowired
    private UserService userService;

    /**
     * Get the currently authenticated user
     * @return Optional<Users> - the current user or empty if not authenticated
     */
    public Users getCurrentUser(Authentication authentication) {

        Users user = null;
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof org.springframework.security.oauth2.core.user.OAuth2User oauth2User) {
                String email = oauth2User.getAttribute("email");
                user = userService.findUserByEmail(email).orElse(null);
            } else {
                String username = authentication.getName();
                user = userService.findUserByUsername(username).orElse(null);
            }
        }
        return user;
    }

    /**
     * Get current user or throw exception if not found
     * @return Users - the current user
     * @throws RuntimeException if user not found or not authenticated
     */


    /**
     * Check if user is currently authenticated
     * @return boolean - true if authenticated
     */
    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null &&
               authentication.isAuthenticated() &&
               !authentication.getName().equals("anonymousUser");
    }

    /**
     * Get current username
     * @return Optional<String> - current username or empty
     */
    public Optional<String> getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        String username = authentication.getName();
        return username != null && !username.equals("anonymousUser") ?
               Optional.of(username) : Optional.empty();
    }
}
