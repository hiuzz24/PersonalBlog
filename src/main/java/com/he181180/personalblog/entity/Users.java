package com.he181180.personalblog.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Getter
@Setter
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

    @Column(name = "password",nullable = false)
    private String password;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "bio")
    private String bio;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "role",nullable = false)
    private String role;

    @Column(name = "created_at",insertable = false)
    private Timestamp createdAt;

    @Column(name = "is_deleted",insertable = false)
    private boolean deleted;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private List<Posts> posts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Comments> comments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<PasswordResetToken> tokens;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "follows",
            joinColumns = @JoinColumn(name = "follower_id"),
            inverseJoinColumns = @JoinColumn(name = "following_id"))
    private Set<Users> following;

    @ManyToMany(mappedBy = "following")
    private Set<Users> followers;

    public boolean isHasPassword() {
        return password != null && !password.isEmpty();
    }

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Notification> receiveNotifications;

    @OneToMany(mappedBy = "fromUser",cascade = CascadeType.ALL)
    private List<Notification> sendNotifications;
}
