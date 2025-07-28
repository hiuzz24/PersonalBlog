package com.he181180.personalblog.security;

import com.he181180.personalblog.entity.Users;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Custom OAuth2User that wraps Users entity for OAuth2 authentication
 * Provides consistent access to user data
 */
public class CustomOAuth2User implements OAuth2User {

    private final OAuth2User oauth2User;
    private final Users user;

    public CustomOAuth2User(OAuth2User oauth2User, Users user) {
        this.oauth2User = oauth2User;
        this.user = user;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oauth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(
            new SimpleGrantedAuthority("ROLE_" + user.getRole().toUpperCase())
        );
    }

    @Override
    public String getName() {
        // CRITICAL: Return username instead of email for consistency
        // If username is not set, return email as fallback
        return user.getUsername() != null ? user.getUsername() : user.getEmail();
    }

    // Get the wrapped Users entity
    public Users getUser() {
        return user;
    }

    // Additional methods for easy access
    public String getEmail() {
        return user.getEmail();
    }

    public String getUsername() {
        return user.getUsername();
    }

    public String getFullName() {
        return user.getFullName();
    }

    public int getUserId() {
        return user.getUserID();
    }

    @Override
    public String toString() {
        return "CustomOAuth2User{" +
                "userId=" + user.getUserID() +
                ", username='" + user.getUsername() + '\'' +
                ", email='" + user.getEmail() + '\'' +
                ", role='" + user.getRole() + '\'' +
                '}';
    }
}
