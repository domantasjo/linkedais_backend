package com.linkedais.backend.dto;

import java.time.LocalDateTime;

public class PostResponse {
    private long id;
    private String content;
    private String imageBase64;
    private LocalDateTime createdAt;
    private int likeCount;

    // Author info
    private long authorId;
    private String authorName;
    private String authorAvatar;
    private int commentCount;

    // Original post info
    private Long originalPostId;
    private Long originalAuthorId;
    private String originalAuthorName;
    private String originalContent;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getImageBase64() { return imageBase64; }
    public void setImageBase64(String imageBase64) { this.imageBase64 = imageBase64; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public long getAuthorId() { return authorId; }
    public void setAuthorId(long authorId) { this.authorId = authorId; }
    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }
    public String getAuthorAvatar() { return authorAvatar; }
    public void setAuthorAvatar(String authorAvatar) { this.authorAvatar = authorAvatar;}
    public int getLikeCount() { return likeCount; }
    public void setLikeCount(int likeCount) { this.likeCount = likeCount; }
    public int getCommentCount() { return commentCount; }
    public void setCommentCount(int commentCount) { this.commentCount = commentCount; }

    public Long getOriginalPostId() { return originalPostId; }
    public void setOriginalPostId(Long originalPostId) { this.originalPostId = originalPostId; }
    public Long getOriginalAuthorId() { return originalAuthorId; }
    public void setOriginalAuthorId(Long originalAuthorId) { this.originalAuthorId = originalAuthorId; }
    public String getOriginalAuthorName() { return originalAuthorName; }
    public void setOriginalAuthorName(String originalAuthorName) { this.originalAuthorName = originalAuthorName; }
    public String getOriginalContent() { return originalContent; }
    public void setOriginalContent(String originalContent) { this.originalContent = originalContent; }
}
