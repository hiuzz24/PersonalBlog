package com.he181180.personalblog.service;

import com.he181180.personalblog.entity.Notification;
import com.he181180.personalblog.entity.Posts;
import com.he181180.personalblog.entity.Users;

import java.util.List;

public interface NotificationService {
    void createNotification(Users user, Users fromUser, String type, Posts post);
    List<Notification> findNotificationsByUserID(int userID);
}
