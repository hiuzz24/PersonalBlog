package com.he181180.personalblog.service;

import com.he181180.personalblog.DTO.MessageDTO;
import com.he181180.personalblog.entity.Messages;
import com.he181180.personalblog.entity.Users;

import java.util.List;

public interface MessageService {

    Messages saveMessage(Messages message);

    List<Messages> getMessagesBetweenUsers(Users user1, Users user2);

    List<Users> getConversationPartners(Users user);

    Messages getLatestMessageBetweenUsers(Users user1, Users user2);

    Long countUnreadMessages(Users user);

    Long countUnreadMessagesFromSender(Users receiver, Users sender);
    
    Long countUnreadMessagesFromUser(Users receiver, Users sender);

    void markMessagesAsRead(Users receiver, Users sender);
}
