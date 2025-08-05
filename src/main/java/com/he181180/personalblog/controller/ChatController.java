package com.he181180.personalblog.controller;

import com.he181180.personalblog.DTO.MessageDTO;
import com.he181180.personalblog.Mapper.MessageMapper;
import com.he181180.personalblog.entity.Messages;
import com.he181180.personalblog.entity.Users;
import com.he181180.personalblog.service.CurrentUserService;
import com.he181180.personalblog.service.MessageService;
import com.he181180.personalblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class ChatController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private CurrentUserService currentUserService;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload MessageDTO messageDTO, Authentication authentication) {
        System.out.println("=== RECEIVED MESSAGE ===");
        System.out.println("MessageDTO: " + messageDTO);
        System.out.println("Authentication object: " + authentication);
        System.out.println("Authentication is null: " + (authentication == null));

        if (authentication != null) {
            System.out.println("Authentication name: " + authentication.getName());
            System.out.println("Authentication principal: " + authentication.getPrincipal());
            System.out.println("Authentication principal type: " + authentication.getPrincipal().getClass().getSimpleName());
            System.out.println("Authentication authorities: " + authentication.getAuthorities());
        }

        try {
            // Use CurrentUserService to get sender consistently
            Users sender = currentUserService.getCurrentUser(authentication);

            System.out.println("Sender: " + sender);
            System.out.println("Sender username: " + (sender != null ? sender.getUsername() : "null"));
            System.out.println("Sender email: " + (sender != null ? sender.getEmail() : "null"));

            if (sender == null) {
                System.out.println("Sender is null, returning");
                return;
            }

            // Get receiver
            Optional<Users> receiverOpt = userService.findUserById(messageDTO.getReceiverId());
            if (receiverOpt.isEmpty()) {
                System.out.println("Receiver not found, returning");
                return;
            }

            Users receiver = receiverOpt.get();
            System.out.println("Receiver: " + receiver.getUsername());
            System.out.println("Receiver username: " + receiver.getUsername());
            System.out.println("Receiver email: " + receiver.getEmail());

            // Convert DTO to entity using MessageMapper
            Messages message = messageMapper.toEntity(messageDTO);
            message.setSender(sender);
            message.setReceiver(receiver);
            message.setIsRead(false);
            message.setCreatedAt(LocalDateTime.now());

            System.out.println("Message entity: " + message);
            Messages savedMessage = messageService.saveMessage(message);

            // Convert back to DTO for response using MessageMapper
            MessageDTO responseDTO = messageMapper.toDTO(savedMessage);

            // CRITICAL FIX: Use username for WebSocket routing (now consistent)
            String senderPrincipal = sender.getUsername();
            String receiverPrincipal = receiver.getUsername();

            System.out.println("Sender principal for WebSocket: " + senderPrincipal);
            System.out.println("Receiver principal for WebSocket: " + receiverPrincipal);

            // Send message to receiver
            messagingTemplate.convertAndSendToUser(
                    receiverPrincipal,
                    "/queue/messages",
                    responseDTO
            );

            // Send confirmation to sender
            messagingTemplate.convertAndSendToUser(
                    senderPrincipal,
                    "/queue/messages",
                    responseDTO
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @GetMapping("/api/chat/conversations")
    @ResponseBody
    public List<Map<String, Object>> getConversations(Authentication authentication) {
        try {
            Users currentUser = currentUserService.getCurrentUser(authentication);
            if (currentUser == null) {
                return List.of();
            }

            List<Users> partners = messageService.getConversationPartners(currentUser);

            return partners.stream().map(partner -> {
                Messages latestMessage = messageService.getLatestMessageBetweenUsers(currentUser, partner);
                long unreadCount = messageService.countUnreadMessagesFromUser(currentUser, partner);
                
                Map<String, Object> conversation = new HashMap<>();
                conversation.put("partnerId", partner.getUserID());
                conversation.put("partnerName", partner.getFullName() != null ? partner.getFullName() : partner.getUsername());
                conversation.put("partnerAvatar", partner.getAvatarUrl() != null ? partner.getAvatarUrl() : "/assets/img/person-1.jpg");
                conversation.put("partnerUsername", partner.getUsername());
                
                if (latestMessage != null) {
                    conversation.put("lastMessage", latestMessage.getContent());
                    conversation.put("lastMessageTime", latestMessage.getCreatedAt());
                    conversation.put("lastMessageSender", latestMessage.getSender().getUsername());
                    conversation.put("isLastMessageFromMe", latestMessage.getSender().getUserID() == currentUser.getUserID());
                } else {
                    conversation.put("lastMessage", "Chưa có tin nhắn");
                    conversation.put("lastMessageTime", null);
                    conversation.put("lastMessageSender", "");
                    conversation.put("isLastMessageFromMe", false);
                }
                
                conversation.put("unreadCount", unreadCount);
                return conversation;
            }).collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }


    /**
     * Lấy lịch sử chat với một user cụ thể
     */
    @GetMapping("/api/chat/messages/{userId}")
    @ResponseBody
    public List<MessageDTO> getChatHistory(@PathVariable int userId, Authentication authentication) {
        try {
            Users currentUser = currentUserService.getCurrentUser(authentication);
            Optional<Users> otherUserOpt = userService.findUserById(userId);

            if (currentUser == null || otherUserOpt.isEmpty()) {
                return List.of();
            }

            Users otherUser = otherUserOpt.get();

            // Mark messages as read
            messageService.markMessagesAsRead(currentUser, otherUser);

            List<Messages> messages = messageService.getMessagesBetweenUsers(currentUser, otherUser);
            return messages.stream()
                    .map(messageMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    /**
     * Lấy thông tin user để chat
     */
    @GetMapping("/api/chat/user/{userId}")
    @ResponseBody
    public Map<String, Object> getChatUserInfo(@PathVariable int userId) {
        try {
            Optional<Users> userOpt = userService.findUserById(userId);
            if (userOpt.isEmpty()) {
                return Map.of();
            }

            Users user = userOpt.get();
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("userId", user.getUserID());
            userInfo.put("username", user.getUsername());
            userInfo.put("fullName", user.getFullName() != null ? user.getFullName() : user.getUsername());
            userInfo.put("avatarUrl", user.getAvatarUrl() != null ? user.getAvatarUrl() : "/assets/img/user.png");
            
            return userInfo;

        } catch (Exception e) {
            e.printStackTrace();
            return Map.of();
        }
    }

    /**
     * Đếm tổng số tin nhắn chưa đọc
     */
    @GetMapping("/api/chat/unread-count")
    @ResponseBody
    public Long getUnreadCount(Authentication authentication) {
        try {
            Users currentUser = currentUserService.getCurrentUser(authentication);
            if (currentUser == null) {
                return 0L;
            }
            return messageService.countUnreadMessages(currentUser);

        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * Lấy thông tin user hiện tại
     */
    @GetMapping("/api/user/current")
    @ResponseBody
    public Map<String, Object> getCurrentUser(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        try {
            Users user = currentUserService.getCurrentUser(authentication);
            if (user != null) {
                response.put("username", user.getUsername());
                response.put("userId", user.getUserID());
                response.put("fullName", user.getFullName());
                response.put("avatarUrl", user.getAvatarUrl());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * Trang chat chính
     */
    @GetMapping("/admin/chat")
    @PreAuthorize("hasRole('ADMIN')")
    public String chatPageForAdmin() {
        return "AdminDashboard/chatadmin";  // Giao diện riêng cho admin
    }
    @GetMapping("/chat")
    public String chatPage() {
        return "UserDashboard/chat";
    }

    private String getPrincipalIdentifier(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof Users) {
            return ((Users) principal).getUsername();
        } else if (principal instanceof String) {
            return (String) principal;
        } else {
            return null;
        }
    }

    private String getReceiverPrincipalIdentifier(Users receiver) {
        return receiver.getUsername();
    }
}
