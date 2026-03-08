package com.linkedais.backend.controller;

import com.linkedais.backend.dto.CreatePostRequest;
import com.linkedais.backend.dto.PostResponse;
import com.linkedais.backend.model.Post;
import com.linkedais.backend.service.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Autowired
    private PostService postService;


    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody @Valid CreatePostRequest request, Principal principal) {

        PostResponse response = postService.createPost(request, principal.getName());
        return ResponseEntity.status(201).body(response);
    }
}
