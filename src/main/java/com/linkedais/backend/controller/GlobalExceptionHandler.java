package com.linkedais.backend.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, Object>> handleMethodNotSupported(
            HttpRequestMethodNotSupportedException e,
            HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(Map.of(
                "error", "Request method is not supported for this endpoint",
                "path", request.getRequestURI(),
                "method", request.getMethod(),
                "allowedMethods", e.getSupportedMethods() == null ? new String[]{} : Arrays.asList(e.getSupportedMethods())
        ));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException e) {
        if (e.getMessage().equals("Post not found")) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
        if (e.getMessage().equals("You can only delete your own posts")) {
            return ResponseEntity.status(403).body(Map.of("error", e.getMessage()));
        }
        if (e.getMessage().equals("User not found")) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
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
