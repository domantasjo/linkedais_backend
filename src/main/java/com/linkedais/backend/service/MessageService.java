package com.linkedais.backend.service;

import com.linkedais.backend.dto.MessageResponse;
import com.linkedais.backend.model.Message;
import com.linkedais.backend.model.User;
import com.linkedais.backend.repository.MessageRepository;
import com.linkedais.backend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public MessageService(MessageRepository messageRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    public MessageResponse sendMessage(Long senderId, Long receiverId, String content) {
        if (senderId.equals(receiverId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Users cannot send messages to themselves.");
        }

        User sender = findUserOrThrow(senderId);
        User receiver = findUserOrThrow(receiverId);
        validateContent(content);

        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content.trim());

        return MessageResponse.from(messageRepository.save(message));
    }

    public MessageResponse replyToMessage(Long parentMessageId, Long senderId, String content) {
        Message parent = findMessageOrThrow(parentMessageId);

        boolean isSender   = parent.getSender().getId().equals(senderId);
        boolean isReceiver = parent.getReceiver().getId().equals(senderId);
        if (!isSender && !isReceiver) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You are not a participant in this conversation.");
        }

        validateContent(content);

        User replySender   = findUserOrThrow(senderId);
        User replyReceiver = isSender ? parent.getReceiver() : parent.getSender();

        Message reply = new Message();
        reply.setSender(replySender);
        reply.setReceiver(replyReceiver);
        reply.setContent(content.trim());
        reply.setParentMessage(parent);

        return MessageResponse.from(messageRepository.save(reply));
    }

    public void deleteMessage(Long messageId, Long requesterId) {
        Message message = findMessageOrThrow(messageId);
        if(!message.getSender().getId().equals(requesterId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only delete messages you sent.");
        }

        message.setDeleted(true);
        messageRepository.save(message);
    }

    public List<MessageResponse> getConversation(Long userAId, Long userBId) {
        User userA = findUserOrThrow(userAId);
        User userB = findUserOrThrow(userBId);
        return messageRepository.findConversation(userA, userB)
                .stream()
                .map(MessageResponse::from)
                .toList();
    }

    public List<MessageResponse> getReplies(Long parentMessageId) {
        findMessageOrThrow(parentMessageId);
        return messageRepository.findReplies(parentMessageId)
                .stream()
                .map(MessageResponse::from)
                .toList();
    }
    public List<MessageResponse> getConversations(Long userId) {
        User user = findUserOrThrow(userId);

        List<Message> sent = messageRepository.findBySenderOrderByCreatedAtAsc(user);
        List<Message> received = messageRepository.findByReceiverOrderByCreatedAtAsc(user);

        Map<Long, Message> latestByPartner = new LinkedHashMap<>();

        Stream.concat(sent.stream(), received.stream())
                .filter(m -> !m.isDeleted())
                .sorted(Comparator.comparing(Message::getCreatedAt))
                .forEach(m -> {
                    Long partnerId = m.getSender().getId().equals(userId)
                            ? m.getReceiver().getId()
                            : m.getSender().getId();
                    latestByPartner.put(partnerId, m);
                });

        return latestByPartner.values().stream()
                .sorted(Comparator.comparing(Message::getCreatedAt).reversed())
                .map(MessageResponse::from)
                .toList();
    }

    private User findUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + userId));
    }

    private Message findMessageOrThrow(Long messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Message not found with id: " + messageId));
    }

    private void validateContent(String content) {
        if (content == null || content.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Message content cannot be empty.");
        }
    }
}
