package com.he181180.personalblog.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class MessageDTO {
    private Long messageId;
    private int senderId;
    private String senderName;
    private int receiverId;
    private String receiverName;
    private String content;
    private LocalDateTime createdAt;
    private Boolean isRead;
}
