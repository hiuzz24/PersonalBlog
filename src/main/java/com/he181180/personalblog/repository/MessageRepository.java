package com.he181180.personalblog.repository;

import com.he181180.personalblog.entity.Messages;
import com.he181180.personalblog.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public interface MessageRepository extends JpaRepository<Messages, Long> {
    
    // Find all messages between two users
    @Query("SELECT m FROM Messages m WHERE " +
           "(m.sender = :user1 AND m.receiver = :user2) OR " +
           "(m.sender = :user2 AND m.receiver = :user1) " +
           "ORDER BY m.createdAt ASC")
    List<Messages> findMessagesBetweenUsers(@Param("user1") Users user1, @Param("user2") Users user2);

    // Trong MessageRepository.java
    @Query("SELECT DISTINCT m.receiver FROM Messages m WHERE m.sender = :user")
    List<Users> findReceiversByUser(@Param("user") Users user);

    @Query("SELECT DISTINCT m.sender FROM Messages m WHERE m.receiver = :user")
    List<Users> findSendersByUser(@Param("user") Users user);
    
    // Get latest message between two users
    @Query("SELECT m FROM Messages m WHERE " +
           "(m.sender = :user1 AND m.receiver = :user2) OR " +
           "(m.sender = :user2 AND m.receiver = :user1) " +
           "ORDER BY m.createdAt DESC")
    List<Messages> findLatestMessageBetweenUsersQuery(@Param("user1") Users user1, @Param("user2") Users user2);


    default Messages findLatestMessageBetweenUsers(Users user1, Users user2) {
        List<Messages> messages = findLatestMessageBetweenUsersQuery(user1, user2);
        return messages.isEmpty() ? null : messages.get(0);
    }
    
    // Count unread messages for a user
    @Query("SELECT COUNT(m) FROM Messages m WHERE m.receiver = :user AND m.isRead = false")
    Long countUnreadMessages(@Param("user") Users user);
    
    // Count unread messages from specific sender
    @Query("SELECT COUNT(m) FROM Messages m WHERE m.receiver = :receiver AND m.sender = :sender AND m.isRead = false")
    Long countUnreadMessagesFromSender(@Param("receiver") Users receiver, @Param("sender") Users sender);
    
    // Mark messages as read - FIXED: Added @Transactional and proper return type
    @Modifying
    @Transactional
    @Query("UPDATE Messages m SET m.isRead = true WHERE m.receiver = :receiver AND m.sender = :sender AND m.isRead = false")
    int markMessagesAsRead(@Param("receiver") Users receiver, @Param("sender") Users sender);
}
