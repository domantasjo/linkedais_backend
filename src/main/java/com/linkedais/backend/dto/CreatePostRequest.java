package com.linkedais.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class CreatePostRequest {
    @NotBlank(message = "Content cannot be empty")
    private String content;

    private String imageBase64;

    private Long originalPostId;

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getImageBase64() { return imageBase64; }
    public void setImageBase64(String imageBase64) { this.imageBase64 = imageBase64; }

    public Long getOriginalPostId() { return originalPostId; }
    public void setOriginalPostId(Long originalPostId) { this.originalPostId = originalPostId; }
}
