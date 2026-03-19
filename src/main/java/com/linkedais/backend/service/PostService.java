package com.linkedais.backend.service;

import com.linkedais.backend.dto.CreatePostRequest;
import com.linkedais.backend.dto.PostResponse;
import com.linkedais.backend.model.Post;
import com.linkedais.backend.model.User;
import com.linkedais.backend.repository.CommentRepository;
import com.linkedais.backend.repository.PostRepository;
import com.linkedais.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentRepository commentRepository;

    public PostResponse createPost(CreatePostRequest request, String email) {
        // 1. Find the user by email from the JWT token
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User no found"));

        // 2. Create a new Post object and fill it with data
        Post post = new Post();
        post.setContent(request.getContent());
        post.setAuthor(user);

        // 3. Save to database — Spring generates the ID and timestamps automatically
        // We need to use saved not post when building the response — otherwise id and createdAt would be null!
        Post saved =  postRepository.save(post);

        // 4. Convert saved Post entity into PostResponse DTO
        PostResponse response = new PostResponse();
        response.setId(saved.getId());
        response.setContent(saved.getContent());
        response.setCreatedAt(saved.getCreatedAt());
        response.setAuthorId(saved.getAuthor().getId());
        response.setAuthorName(saved.getAuthor().getName());
        response.setCommentCount(0);
        return response;
    }
    public List<PostResponse> getAllPosts(int page, int size) {
        // 1. Fetch all posts ordered by newest first
        Pageable pageable = PageRequest.of(page, size);
        List<Post> posts = postRepository.findAllByOrderByCreatedAtDesc(pageable).getContent();
        List<PostResponse> postResponses = new ArrayList<>();

        // 2. Loop through each post and convert to PostResponse DTO
        for (Post post : posts) {
            PostResponse response = new PostResponse();
            response.setId( post.getId());
            response.setContent(post.getContent());
            response.setCreatedAt(post.getCreatedAt());
            response.setAuthorId(post.getAuthor().getId());
            response.setAuthorName(post.getAuthor().getName());
            response.setCommentCount(commentRepository.countByPostId(post.getId()));
            postResponses.add(response);
        }
        // 3. Return the full list to the controller
        return postResponses;
    }
    public void deletePostById(Long id, String email) {
        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
        if (!post.getAuthor().getEmail().equals(email))
        {
            throw new RuntimeException("You can only delete your own posts");
        }
        postRepository.deleteById(id);
    }
}
