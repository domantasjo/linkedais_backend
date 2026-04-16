package com.linkedais.backend.controller;

import com.linkedais.backend.dto.MessageResponse;
import com.linkedais.backend.dto.ReplyRequest;
import com.linkedais.backend.dto.SendMessageRequest;
import com.linkedais.backend.model.Message;
import com.linkedais.backend.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    @Autowired
    private MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public ResponseEntity<MessageResponse> sendMessage(@RequestBody SendMessageRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(messageService.sendMessage(request.senderId(), request.receiverId(), request.content()));
    }
    @PostMapping("/{id}/reply")
    public ResponseEntity<MessageResponse> replyToMessage(
            @PathVariable Long id,
            @RequestBody ReplyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(messageService.replyToMessage(id, request.senderId(), request.content()));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(
            @PathVariable Long id,
            @RequestParam Long requesterId) {
        messageService.deleteMessage(id, requesterId);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/conversation")
    public ResponseEntity<List<MessageResponse>> getConversation(
            @RequestParam Long userAId,
            @RequestParam Long userBId) {
        return ResponseEntity.ok(messageService.getConversation(userAId, userBId));
    }
    @GetMapping("/conversations")
    public ResponseEntity<List<MessageResponse>> getConversations(@RequestParam Long userId) {
        return ResponseEntity.ok(messageService.getConversations(userId));
    }
    @GetMapping("/{id}/replies")
    public ResponseEntity<List<MessageResponse>> getReplies(@PathVariable Long id) {
        return ResponseEntity.ok(messageService.getReplies(id));
    }
}

