package com.he181180.personalblog.service.impl;

import com.he181180.personalblog.entity.Favorites;
import com.he181180.personalblog.entity.Posts;
import com.he181180.personalblog.entity.Users;
import com.he181180.personalblog.repository.FavoriteRepository;
import com.he181180.personalblog.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Override
    public Favorites addToFavorites(Users user, Posts post) {
        // Check if already exists
        if (favoriteRepository.existsByUserAndPost(user, post)) {
            throw new IllegalStateException("Post is already in favorites");
        }

        Favorites favorite = Favorites.builder()
                .user(user)
                .post(post)
                .build();

        return favoriteRepository.save(favorite);
    }

    @Override
    public void removeFromFavorites(Users user, Posts post) {
        favoriteRepository.deleteByUserAndPost(user, post);
    }

    @Override
    public boolean isPostFavorited(Users user, Posts post) {
        return favoriteRepository.existsByUserAndPost(user, post);
    }

    @Override
    public List<Favorites> getUserFavorites(Users user) {
        return favoriteRepository.findByUserOrderByCreatedAtDesc(user);
    }

    @Override
    public long countUserFavorites(Users user) {
        return favoriteRepository.countByUser(user);
    }

    @Override
    public boolean toggleFavorite(Users user, Posts post) {
        Optional<Favorites> existingFavorite = favoriteRepository.findByUserAndPost(user, post);

        if (existingFavorite.isPresent()) {
            // Remove from favorites
            favoriteRepository.delete(existingFavorite.get());
            return false; // Not favorited anymore
        } else {
            // Add to favorites
            addToFavorites(user, post);
            return true; // Now favorited
        }
    }
}
