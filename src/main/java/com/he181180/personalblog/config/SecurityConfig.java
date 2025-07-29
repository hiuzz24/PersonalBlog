package com.he181180.personalblog.config;

import com.he181180.personalblog.DTO.UserUpdateDTO;
import com.he181180.personalblog.entity.Users;
import com.he181180.personalblog.repository.UserRepository;
import com.he181180.personalblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CustomSuccessHandler customSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/oauth2/**",
                                         "/register", "/img/**",
                                         "/css/**","/js/**","/assets/**", "/favicon.ico", "/forgotPassword", "/resetPassword", "/savePassword").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/login")
                        .failureUrl("/login?error")
                        .successHandler(customSuccessHandler)
                        .permitAll()
                ).oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .defaultSuccessUrl("/GoogleLogin", true)  //
                )

                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );
           http.csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Users u = userRepository.findByUsernameAndDeletedFalse(username)
                    .orElseThrow(() -> new UsernameNotFoundException("user not found"));

            return User.withUsername(u.getUsername())
                    .password(u.getPassword() == null ? "" : u.getPassword())
                    .roles(u.getRole())
                    .build();
        };

    }


}
