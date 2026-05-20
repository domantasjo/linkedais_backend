package com.linkedais.backend.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table (name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @Column(name = "image_base64", columnDefinition = "TEXT")
    private String imageBase64;

    @ManyToOne
    @JoinColumn(name = "user_id",  nullable = false)
    private User author;

    @ManyToOne
    @JoinColumn(name = "original_post_id")
    private Post originalPost;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate()
    {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getImageBase64() { return imageBase64; }
    public void setImageBase64(String imageBase64) { this.imageBase64 = imageBase64; }

    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }

    public Post getOriginalPost() { return originalPost; }
    public void setOriginalPost(Post originalPost) { this.originalPost = originalPost; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}
