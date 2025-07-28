package com.he181180.personalblog.security;

import com.he181180.personalblog.entity.Users;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Custom UserPrincipal that wraps Users entity
 * Provides consistent authentication for both regular login and OAuth2
 */
public class CustomUserPrincipal implements UserDetails {

    private final Users user;

    public CustomUserPrincipal(Users user) {
        this.user = user;
    }

    // Get the wrapped Users entity
    public Users getUser() {
        return user;
    }

    // UserDetails implementation
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(
            new SimpleGrantedAuthority("ROLE_" + user.getRole().toUpperCase())
        );
    }

    @Override
    public String getPassword() {
        return user.getPassword() != null ? user.getPassword() : "";
    }

    @Override
    public String getUsername() {
        // Always return username for consistency
        return user.getUsername();
    }

    // Additional methods to get user info
    public String getEmail() {
        return user.getEmail();
    }

    public String getFullName() {
        return user.getFullName();
    }

    public int getUserId() {
        return user.getUserID();
    }

    @Override
    public boolean isAccountNonExpired() {
        return !user.isDeleted();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !user.isDeleted();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !user.isDeleted();
    }

    @Override
    public String toString() {
        return "CustomUserPrincipal{" +
                "userId=" + user.getUserID() +
                ", username='" + user.getUsername() + '\'' +
                ", email='" + user.getEmail() + '\'' +
                ", role='" + user.getRole() + '\'' +
                '}';
    }
}
