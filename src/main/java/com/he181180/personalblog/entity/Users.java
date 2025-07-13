package com.he181180.personalblog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userID;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "bio")
    private String bio;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "role")
    private String role;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private List<Posts> posts;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private List<Comments> comments;
}
