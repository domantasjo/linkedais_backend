package com.linkedais.backend.controller;

import com.linkedais.backend.model.Post;
import com.linkedais.backend.service.BookmarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkController {

    @Autowired
    private BookmarkService bookmarkService;

    @PostMapping("/{postId}")
    public ResponseEntity<Void> addBookmark(@PathVariable Long postId, Principal principal) {
        bookmarkService.addBookmark(postId, principal.getName());
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> removeBookmark(@PathVariable Long postId, Principal principal) {
        bookmarkService.removeBookmark(postId, principal.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<Post>> getUserBookmarks(Principal principal, Pageable pageable) {
        Page<Post> bookmarks = bookmarkService.getUserBookmarks(principal.getName(), pageable);
        return ResponseEntity.ok(bookmarks);
    }
}