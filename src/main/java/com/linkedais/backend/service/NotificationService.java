package com.linkedais.backend.service;

import com.linkedais.backend.dto.NotificationResponse;
import com.linkedais.backend.model.Comment;
import com.linkedais.backend.model.Notification;
import com.linkedais.backend.model.Post;
import com.linkedais.backend.model.User;
import com.linkedais.backend.repository.CommentRepository;
import com.linkedais.backend.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final CommentRepository commentRepository;

    public NotificationService(NotificationRepository notificationRepository, CommentRepository commentRepository) {
        this.notificationRepository = notificationRepository;
        this.commentRepository = commentRepository;
    }

    public void createCommentNotifications(Post post, Comment newComment, User commentAuthor) {
        Set<Long> notifiedUserIds = new HashSet<>();

        // 1. Notify the post author (if not the comment author)
        if (!post.getAuthor().getId().equals(commentAuthor.getId())) {
            Notification notification = new Notification();
            notification.setUser(post.getAuthor());
            notification.setType("COMMENT");
            notification.setMessage(commentAuthor.getName() + " pakomentavo jūsų įrašą");
            notification.setPostId(post.getId());
            notification.setCommentId(newComment.getId());
            notificationRepository.save(notification);
            notifiedUserIds.add(post.getAuthor().getId());
        }

        // 2. Notify other commenters on the same post (excluding comment author and already notified)
        List<Comment> existingComments = commentRepository.findByPostId(post.getId());
        for (Comment existingComment : existingComments) {
            Long existingAuthorId = existingComment.getAuthor().getId();
            if (!existingAuthorId.equals(commentAuthor.getId()) && !notifiedUserIds.contains(existingAuthorId)) {
                Notification notification = new Notification();
                notification.setUser(existingComment.getAuthor());
                notification.setType("COMMENT_REPLY");
                notification.setMessage(commentAuthor.getName() + " atsakė į jūsų komentarą");
                notification.setPostId(post.getId());
                notification.setCommentId(newComment.getId());
                notificationRepository.save(notification);
                notifiedUserIds.add(existingAuthorId);
            }
        }
    }

    public List<NotificationResponse> getUserNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private NotificationResponse toResponse(Notification n) {
        return new NotificationResponse(
                n.getId(),
                n.getUser().getId(),
                n.getType(),
                n.getMessage(),
                n.getPostId(),
                n.getCommentId(),
                n.isRead(),
                n.getCreatedAt(),
                n.getConnectionId()
        );
    }
    public void markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }
    public List<NotificationResponse> getUnreadNotifications(Long userId) {
        return notificationRepository.findByUserIdAndReadFalseOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public void markAllAsRead(Long userId) {
        List<Notification> unread = notificationRepository.findByUserIdAndReadFalseOrderByCreatedAtDesc(userId);
        for (Notification notification : unread) {
            notification.setRead(true);
        }
        notificationRepository.saveAll(unread);
    }
}
