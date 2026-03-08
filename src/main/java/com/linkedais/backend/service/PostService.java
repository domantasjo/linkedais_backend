package com.linkedais.backend.service;

import com.linkedais.backend.dto.CreatePostRequest;
import com.linkedais.backend.dto.PostResponse;
import com.linkedais.backend.model.Post;
import com.linkedais.backend.model.User;
import com.linkedais.backend.repository.PostRepository;
import com.linkedais.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    public PostResponse createPost(CreatePostRequest request, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User no found"));

        Post post = new Post();
        post.setContent(request.getContent());
        post.setAuthor(user);

        Post saved =  postRepository.save(post);

        PostResponse response = new PostResponse();
        response.setId(saved.getId());
        response.setContent(saved.getContent());
        response.setCreatedAt(saved.getCreatedAt());
        response.setAuthorId(saved.getAuthor().getId());
        response.setAuthorName(saved.getAuthor().getEmail());
        return response;
    }
}
