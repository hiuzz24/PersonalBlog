package com.he181180.personalblog.Mapper;

import com.he181180.personalblog.DTO.MessageDTO;
import com.he181180.personalblog.entity.Messages;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    /**
     * Convert Messages entity to MessageDTO
     * @param message Messages entity
     * @return MessageDTO
     */
    @Mapping(source = "messageId", target = "messageId")
    @Mapping(source = "sender.userID", target = "senderId")
    @Mapping(source = "sender.username", target = "senderName")
    @Mapping(source = "receiver.userID", target = "receiverId")
    @Mapping(source = "receiver.username", target = "receiverName")
    @Mapping(source = "content", target = "content")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "isRead", target = "isRead")
    MessageDTO toDTO(Messages message);

    /**
     * Convert MessageDTO to Messages entity
     * @param messageDTO MessageDTO
     * @return Messages entity
     */
    @Mapping(source = "messageId", target = "messageId")
    @Mapping(target = "sender", ignore = true)
    @Mapping(target = "receiver", ignore = true)
    @Mapping(source = "content", target = "content")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "isRead", target = "isRead")
    Messages toEntity(MessageDTO messageDTO);
}
