package com.he181180.personalblog.service.impl;

import com.he181180.personalblog.entity.Notification;
import com.he181180.personalblog.entity.Posts;
import com.he181180.personalblog.entity.Users;
import com.he181180.personalblog.repository.NotificationRepository;
import com.he181180.personalblog.service.NotificationService;
import com.he181180.personalblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private UserService userService;
    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public void createNotification(Users user, Users fromUser, String type, Posts post) {
            if(type.equals("newPost")) {
                List<Users> follower = userService.findFollowerByUserID(fromUser.getUserID());
                for(Users follow : follower){
                Notification notification = Notification.builder()
                        .user(follow)
                        .fromUser(fromUser)
                        .type("newPost")
                        .message(fromUser.getUsername() + " posted a new post")
                        .createdAt(new Timestamp(new Date().getTime()))
                        .isRead(false)
                        .post(post)
                        .build();
                notificationRepository.save(notification);
            }
            }else if(type.equals("follow")){
                Notification notification = Notification.builder()
                        .user(user)
                        .fromUser(fromUser)
                        .type("follow")
                        .message(fromUser.getUsername()+" is now following you")
                        .createdAt(new Timestamp(new Date().getTime()))
                        .isRead(false)
                        .post(null)
                        .build();
                notificationRepository.save(notification);
            }
    }

    @Override
    public List<Notification> findNotificationsByUserID(int userID) {
        return notificationRepository.findNotificationsByUser_UserID(userID);
    }

}
