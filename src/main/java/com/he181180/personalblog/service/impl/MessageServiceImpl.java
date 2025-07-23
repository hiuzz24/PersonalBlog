package com.he181180.personalblog.service.impl;

import com.he181180.personalblog.DTO.MessageDTO;
import com.he181180.personalblog.entity.Messages;
import com.he181180.personalblog.entity.Users;
import com.he181180.personalblog.repository.MessageRepository;
import com.he181180.personalblog.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public Messages saveMessage(Messages message) {
        return messageRepository.save(message);
    }

    @Override
    public List<Messages> getMessagesBetweenUsers(Users user1, Users user2) {
        return messageRepository.findMessagesBetweenUsers(user1, user2);
    }

    @Override
    public List<Users> getConversationPartners(Users user) {
        return messageRepository.findConversationPartners(user);
    }

    @Override
    public Messages getLatestMessageBetweenUsers(Users user1, Users user2) {
        return messageRepository.findLatestMessageBetweenUsers(user1, user2);
    }

    @Override
    public Long countUnreadMessages(Users user) {
        return messageRepository.countUnreadMessages(user);
    }

    @Override
    public Long countUnreadMessagesFromSender(Users receiver, Users sender) {
        return messageRepository.countUnreadMessagesFromSender(receiver, sender);
    }

    @Override
    public Long countUnreadMessagesFromUser(Users receiver, Users sender) {
        return messageRepository.countUnreadMessagesFromSender(receiver, sender);
    }

    /**
     * Marks all messages from sender to receiver as read. This is a transactional
     * operation and will roll back if any error occurs.
     *
     * @param receiver the user who received the messages
     * @param sender   the user who sent the messages
     */
    @Override
    @Transactional
    public void markMessagesAsRead(Users receiver, Users sender) {
        messageRepository.markMessagesAsRead(receiver, sender);
    }

}
