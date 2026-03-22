package com.linkedais.backend.controller;

import com.linkedais.backend.model.Like;
import com.linkedais.backend.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/posts/{postId}/likes")
public class LikeController {
    @Autowired
    private LikeService likeService;

    @PostMapping
    public ResponseEntity<Void> likePost(@PathVariable Long postId, Principal principal)
    {
        likeService.likePost(postId, principal.getName());
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> unlikePost(@PathVariable Long postId, Principal principal) {
        likeService.unlikePost(postId, principal.getName());
        return ResponseEntity.noContent().build();
    }
}
