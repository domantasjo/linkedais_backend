package com.linkedais.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "bookmarks",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "post_id"})
        })
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    public Bookmark() {}

    public Bookmark(User user, Post post) {
        this.user = user;
        this.post = post;
    }

    // Getters
    public Long getId() { return id; }
    public User getUser() { return user; }
    public Post getPost() { return post; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setUser(User user) { this.user = user; }
    public void setPost(Post post) { this.post = post; }
}