package com.he181180.personalblog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private int notificationID;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @Column(name = "type")
    private String type;

    @ManyToOne
    @JoinColumn(name = "from_user_id")
    private Users fromUser;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Posts post;

    @Column(name = "message")
    private String message;

    @Column(name = "is_read")
    private boolean isRead;

    @Column(name = "created_at")
    private Timestamp createdAt;
}
