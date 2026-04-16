package com.linkedais.backend.dto;

public record SendMessageRequest(Long senderId, Long receiverId, String content) {}
