package com.linkedais.backend.controller;

import com.linkedais.backend.dto.CommentResponse;
import com.linkedais.backend.dto.CreateCommentRequest;
import com.linkedais.backend.dto.UpdateCommentRequest;
import com.linkedais.backend.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentsByPostId(postId));
    }

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable Long postId,
            @Valid @RequestBody CreateCommentRequest request,
            Principal principal) {
        CommentResponse response = commentService.createComment(postId, request, principal.getName());
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @Valid @RequestBody UpdateCommentRequest request,
            Principal principal) {
        CommentResponse response = commentService.updateComment(postId, commentId, request, principal.getName());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            Principal principal) {
        commentService.deleteComment(postId, commentId, principal.getName());
        return ResponseEntity.noContent().build();
    }
}
