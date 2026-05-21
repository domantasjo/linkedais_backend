package com.linkedais.backend.controller;

import com.linkedais.backend.dto.TranscriptDTO;
import com.linkedais.backend.service.TranscriptService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Academic Controller
 *
 * Endpoints related to a student's academic history.
 * All endpoints are PROTECTED (require valid JWT token).
 */
@RestController
@RequestMapping("/api/academic")
public class AcademicController {

    private final TranscriptService transcriptService;

    public AcademicController(TranscriptService transcriptService) {
        this.transcriptService = transcriptService;
    }

    /**
     * GET CURRENT STUDENT'S TRANSCRIPT
     * GET /api/academic/transcript
     *
     * Returns the full academic history grouped by semester, including
     * course name, code, credits, grade, per-semester GPA and overall GPA.
     */
    @GetMapping("/transcript")
    public ResponseEntity<TranscriptDTO> getMyTranscript(Authentication authentication) {
        return ResponseEntity.ok(transcriptService.getTranscriptForEmail(authentication.getName()));
    }
}
