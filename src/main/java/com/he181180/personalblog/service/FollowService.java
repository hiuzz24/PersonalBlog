package com.he181180.personalblog.service;

import org.springframework.security.core.Authentication;

public interface FollowService {
    void followUser(int userId,Authentication auth);

    void unfollowUser(int userId, Authentication auth);

    boolean isFollowing(int userId, Authentication auth);
}

