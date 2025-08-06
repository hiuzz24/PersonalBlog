package com.he181180.personalblog.service;

import com.he181180.personalblog.DTO.UserUpdateDTO;
import com.he181180.personalblog.entity.Users;
import com.he181180.personalblog.security.CustomOAuth2User;
import com.he181180.personalblog.security.CustomUserPrincipal;
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
     * Works with both CustomUserPrincipal (regular login) and CustomOAuth2User (OAuth2 login)
     * @return Users - the current user or null if not authenticated
     */
    public Users getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        // Handle CustomUserPrincipal (regular login)
        if (principal instanceof CustomUserPrincipal customUserPrincipal) {
            return customUserPrincipal.getUser();
        }

        // Handle CustomOAuth2User (OAuth2 login)
        if (principal instanceof CustomOAuth2User customOAuth2User) {
            return customOAuth2User.getUser();
        }

        // Fallback for legacy authentication (should not happen after migration)
        if (principal instanceof org.springframework.security.oauth2.core.user.OAuth2User oauth2User) {
            String email = oauth2User.getAttribute("email");
            return userService.findUserByEmail(email).orElse(null);
        }

        // Fallback for UserDetails (should not happen after migration)
        if (principal instanceof org.springframework.security.core.userdetails.UserDetails userDetails) {
            String username = userDetails.getUsername();
            return userService.findUserByUsername(username).orElse(null);
        }

        return null;
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
     * Get current username - now always consistent
     * @return Optional<String> - current username or empty
     */
    public Optional<String> getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users currentUser = getCurrentUser(authentication);

        if (currentUser != null && currentUser.getUsername() != null) {
            return Optional.of(currentUser.getUsername());
        }

        return Optional.empty();
    }

    /**
     * Get current user ID
     * @return Optional<Integer> - current user ID or empty
     */
    public Optional<Integer> getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users currentUser = getCurrentUser(authentication);

        if (currentUser != null) {
            return Optional.of(currentUser.getUserID());
        }

        return Optional.empty();
    }

    /**
     * Get current user email
     * @return Optional<String> - current user email or empty
     */
    public Optional<String> getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users currentUser = getCurrentUser(authentication);

        if (currentUser != null && currentUser.getEmail() != null) {
            return Optional.of(currentUser.getEmail());
        }

        return Optional.empty();
    }
}
