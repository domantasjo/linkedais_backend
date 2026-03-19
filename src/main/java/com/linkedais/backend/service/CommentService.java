package com.linkedais.backend.service;

import com.linkedais.backend.dto.CommentResponse;
import com.linkedais.backend.dto.CreateCommentRequest;
import com.linkedais.backend.dto.UpdateCommentRequest;
import com.linkedais.backend.model.Comment;
import com.linkedais.backend.model.Post;
import com.linkedais.backend.model.User;
import com.linkedais.backend.repository.CommentRepository;
import com.linkedais.backend.repository.PostRepository;
import com.linkedais.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository,
                          UserRepository userRepository, NotificationService notificationService) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    public List<CommentResponse> getCommentsByPostId(Long postId) {
        postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        return commentRepository.findByPostIdOrderByCreatedAtAsc(postId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public CommentResponse createComment(Long postId, CreateCommentRequest request, String email) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        User author = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setAuthor(author);
        comment.setContent(request.getContent());

        Comment saved = commentRepository.save(comment);

        // Create notifications
        notificationService.createCommentNotifications(post, saved, author);

        return toResponse(saved);
    }

    public CommentResponse updateComment(Long postId, Long commentId, UpdateCommentRequest request, String email) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getPost().getId().equals(postId)) {
            throw new RuntimeException("Comment not found");
        }

        if (!comment.getAuthor().getEmail().equals(email)) {
            throw new RuntimeException("Forbidden");
        }

        comment.setContent(request.getContent());
        Comment updated = commentRepository.save(comment);

        return toResponse(updated);
    }

    public void deleteComment(Long postId, Long commentId, String email) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getPost().getId().equals(postId)) {
            throw new RuntimeException("Comment not found");
        }

        if (!comment.getAuthor().getEmail().equals(email)) {
            throw new RuntimeException("Forbidden");
        }

        commentRepository.delete(comment);
    }

    private CommentResponse toResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getPost().getId(),
                comment.getAuthor().getId(),
                comment.getAuthor().getName(),
                comment.getContent(),
                comment.getCreatedAt()
        );
    }
}
