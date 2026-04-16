package com.linkedais.backend.dto;

import com.linkedais.backend.model.Message;

import java.time.LocalDateTime;

public record MessageResponse(
        Long id,
        Long senderId,
        String senderName,
        Long receiverId,
        String receiverName,
        String content,
        Long parentMessageId,
        boolean deleted,
        LocalDateTime createdAt
) {
    public static MessageResponse from(Message message) {
        return new MessageResponse(
                message.getId(),
                message.getSender().getId(),
                message.getSender().getName(),
                message.getReceiver().getId(),
                message.getReceiver().getName(),
                message.getContent(),
                message.getParentMessage() != null ? message.getParentMessage().getId() : null,
                message.isDeleted(),
                message.getCreatedAt()
        );
    }
}
