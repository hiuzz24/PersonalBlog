package com.he181180.personalblog.service;

import com.he181180.personalblog.entity.Favorites;
import com.he181180.personalblog.entity.Posts;
import com.he181180.personalblog.entity.Users;

import java.util.List;

public interface FavoriteService {

    // Add a post to favorites
    Favorites addToFavorites(Users user, Posts post);

    // Remove a post from favorites
    void removeFromFavorites(Users user, Posts post);

    // Check if a post is favorited by user
    boolean isPostFavorited(Users user, Posts post);

    // Get all favorite posts for a user
    List<Favorites> getUserFavorites(Users user);

    // Count user's favorites
    long countUserFavorites(Users user);

    // Toggle favorite status (add if not exists, remove if exists)
    boolean toggleFavorite(Users user, Posts post);
}
