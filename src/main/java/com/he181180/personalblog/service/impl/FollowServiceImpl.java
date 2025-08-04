package com.he181180.personalblog.service.impl;

import com.he181180.personalblog.entity.Users;
import com.he181180.personalblog.repository.UserRepository;
import com.he181180.personalblog.service.CurrentUserService;
import com.he181180.personalblog.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final CurrentUserService currentUserService;

    @Override
    @Transactional
    public void followUser(int userId,Authentication authentication) {
        Users currentUser = currentUserService.getCurrentUser(authentication);
        Optional<Users> userToFollowOpt = userRepository.findByUserIDAndDeletedFalse(userId);

        if (userToFollowOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        Users userToFollow = userToFollowOpt.get();
        if (currentUser.getUserID() == userToFollow.getUserID()) {
            throw new RuntimeException("You cannot follow yourself");
        }
        Optional<Users> currentUserWithFollowing = userRepository.findByIdWithFollowing(currentUser.getUserID());
        if (currentUserWithFollowing.isPresent()) {
            currentUserWithFollowing.get().getFollowing().add(userToFollow);
            userRepository.save(currentUserWithFollowing.get());
        } else {
            throw new RuntimeException("Current user not found");
        }
    }

    @Override
    @Transactional
    public void unfollowUser(int userId, Authentication authentication) {
        Users currentUser = currentUserService.getCurrentUser(authentication);
        Optional<Users> userToUnfollowOpt = userRepository.findByUserIDAndDeletedFalse(userId);

        if (userToUnfollowOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        Users userToUnfollow = userToUnfollowOpt.get();
        Optional<Users> currentUserWithFollowing = userRepository.findByIdWithFollowing(currentUser.getUserID());
        if (currentUserWithFollowing.isPresent()) {
            currentUserWithFollowing.get().getFollowing().remove(userToUnfollow);
            userRepository.save(currentUserWithFollowing.get());
        } else {
            throw new RuntimeException("Current user not found");
        }
    }

    @Override
    @Transactional
    public boolean isFollowing(int userId, Authentication authentication) {
        Users currentUser = currentUserService.getCurrentUser(authentication);
        Optional<Users> userToCheckOpt = userRepository.findByUserIDAndDeletedFalse(userId);
        if (userToCheckOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        Users userToCheck = userToCheckOpt.get();
        Optional<Users> currentUserWithFollowing = userRepository.findByIdWithFollowing(currentUser.getUserID());
        return currentUserWithFollowing.map(users -> users.getFollowing().contains(userToCheck)).orElse(false);
    }

    @Override
    @Transactional
    public Set<Users> getFollowers(Authentication authentication) {
        Users currentUser = currentUserService.getCurrentUser(authentication);
        if (currentUser == null) {
            throw new RuntimeException("User not authenticated");
        }
        Optional<Users> userWithFollowers = userRepository.findByIdWithFollowers(currentUser.getUserID());
        return userWithFollowers.map(Users::getFollowers).orElse(Set.of());
    }

    @Override
    @Transactional
    public Set<Users> getFollowing(Authentication authentication) {
        Users currentUser = currentUserService.getCurrentUser(authentication);
        if (currentUser == null) {
            throw new RuntimeException("User not authenticated");
        }
        Optional<Users> userWithFollowing = userRepository.findByIdWithFollowing(currentUser.getUserID());
        return userWithFollowing.map(Users::getFollowing).orElse(Set.of());
    }


    @Override
    public Integer totalFollower(int userID) {
        return userRepository.totalFollower(userID);
    }

    @Override
    public Integer totalFollowing(int userID) {
        return userRepository.totalFollowing(userID);
    }
}
