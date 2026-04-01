package com.linkedais.backend.dto;

import java.time.LocalDateTime;

public class NotificationResponse {
    private Long id;
    private Long userId;
    private String type;
    private String message;
    private Long postId;
    private Long commentId;
    private boolean read;
    private LocalDateTime createdAt;
    private Long connectionId;

    public NotificationResponse() {}

    public NotificationResponse(Long id, Long userId, String type, String message, Long postId, Long commentId, boolean read, LocalDateTime createdAt, Long connectionId) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.message = message;
        this.postId = postId;
        this.commentId = commentId;
        this.read = read;
        this.createdAt = createdAt;
        this.connectionId = connectionId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Long getPostId() { return postId; }
    public void setPostId(Long postId) { this.postId = postId; }

    public Long getCommentId() { return commentId; }
    public void setCommentId(Long commentId) { this.commentId = commentId; }

    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Long getConnectionId() { return connectionId; }
    public void setConnectionId(Long connectionId) { this.connectionId = connectionId; }
}
