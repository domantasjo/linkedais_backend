package com.linkedais.backend.service;

import com.linkedais.backend.model.Like;
import com.linkedais.backend.model.Post;
import com.linkedais.backend.model.User;
import com.linkedais.backend.repository.LikeRepository;
import com.linkedais.backend.repository.PostRepository;
import com.linkedais.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    public void likePost(Long id, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        if (likeRepository.existsByPostIdAndUserId(id, user.getId())) {
            throw new RuntimeException("Post already liked by this user");
        }
        Post post  = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));

        Like like  = new Like();
        like.setPost(post);
        like.setUser(user);
        likeRepository.save(like);
    }
    public void unlikePost(Long id, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        likeRepository.deleteByPostIdAndUserId(id, user.getId());
    }

    public int getLikeCount(Long id)
    {
        return likeRepository.countByPostId(id);
    }
}
