package com.he181180.personalblog.service;

import com.he181180.personalblog.entity.Users;
import org.springframework.security.core.Authentication;

import java.util.Set;

public interface FollowService {
    void followUser(int userId,Authentication auth);

    void unfollowUser(int userId, Authentication auth);

    boolean isFollowing(int userId, Authentication auth);

    Set<Users> getFollowers(Authentication authentication);

    Set<Users> getFollowing(Authentication authentication);
}

