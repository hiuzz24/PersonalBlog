package com.he181180.personalblog.security;

import com.he181180.personalblog.entity.Users;
import com.he181180.personalblog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

/**
 * Custom OAuth2UserService that creates or updates user and returns CustomUserPrincipal
 */
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        return processOAuth2User(oauth2User);
    }

    private OAuth2User processOAuth2User(OAuth2User oauth2User) {
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");

        // Find or create user
        Users user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            // Create new user
            user = new Users();
            user.setEmail(email);
            user.setFullName(name);
            user.setRole("WRITER");
            // Note: username will be set later in complete-username flow
            userRepository.save(user);
        }

        // Return CustomOAuth2User that wraps our Users entity
        return new CustomOAuth2User(oauth2User, user);
    }
}
