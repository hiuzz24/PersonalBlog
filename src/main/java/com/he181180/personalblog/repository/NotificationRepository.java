package com.he181180.personalblog.repository;

import com.he181180.personalblog.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,Integer> {
    List<Notification> findNotificationsByUser_UserIDOrderByCreatedAtDesc(int userID);
}
