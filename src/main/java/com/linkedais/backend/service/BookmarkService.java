package com.linkedais.backend.service;

import com.linkedais.backend.model.Bookmark;
import com.linkedais.backend.model.Post;
import com.linkedais.backend.model.User;
import com.linkedais.backend.repository.BookmarkRepository;
import com.linkedais.backend.repository.PostRepository;
import com.linkedais.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BookmarkService {

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void addBookmark(Long postId, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        if (bookmarkRepository.existsByPostIdAndUserId(postId, user.getId())) {
            throw new RuntimeException("Post already bookmarked by this user");
        }

        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));

        Bookmark bookmark = new Bookmark();
        bookmark.setPost(post);
        bookmark.setUser(user);
        bookmarkRepository.save(bookmark);
    }

    @Transactional
    public void removeBookmark(Long postId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        bookmarkRepository.deleteByPostIdAndUserId(postId, user.getId());
    }

    @Transactional
    public Page<Post> getUserBookmarks(String email, Pageable pageable) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Page<Bookmark> bookmarks = bookmarkRepository.findByUserId(user.getId(), pageable);
        return bookmarks.map(Bookmark::getPost);
    }
}