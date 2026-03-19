package com.linkedais.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException e) {
        if (e.getMessage().equals("Post not found")) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
        if (e.getMessage().equals("You can only delete your own posts")) {
            return ResponseEntity.status(403).body(Map.of("error", e.getMessage()));
        }
        if (e.getMessage().equals("Comment not found")) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
        if (e.getMessage().equals("Forbidden")) {
            return ResponseEntity.status(403).body(Map.of("error", e.getMessage()));
        }
        return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
    }
}
