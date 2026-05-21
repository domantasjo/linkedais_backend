package com.linkedais.backend.controller;

import com.linkedais.backend.dto.WorkExperienceRequest;
import com.linkedais.backend.dto.WorkExperienceResponse;
import com.linkedais.backend.service.WorkExperienceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profile/experience")
public class WorkExperienceController {

    private final WorkExperienceService workExperienceService;

    public WorkExperienceController(WorkExperienceService workExperienceService) {
        this.workExperienceService = workExperienceService;
    }

    /**
     * GET /api/profile/experience/{userId}
     * Get all work experience entries for a user (public endpoint)
     */
    @GetMapping("/{userId}")
    public ResponseEntity<List<WorkExperienceResponse>> getUserWorkExperience(@PathVariable Long userId) {
        return ResponseEntity.ok(workExperienceService.getUserWorkExperiences(userId));
    }

    /**
     * POST /api/profile/experience
     * Add a new work experience entry
     */
    @PostMapping
    public ResponseEntity<WorkExperienceResponse> addWorkExperience(
            Authentication authentication,
            @Valid @RequestBody WorkExperienceRequest request) {
        String email = authentication.getName();
        WorkExperienceResponse response = workExperienceService.addWorkExperience(email, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * PUT /api/profile/experience/{id}
     * Update an existing work experience entry
     */
    @PutMapping("/{id}")
    public ResponseEntity<WorkExperienceResponse> updateWorkExperience(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody WorkExperienceRequest request) {
        String email = authentication.getName();
        WorkExperienceResponse response = workExperienceService.updateWorkExperience(email, id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /api/profile/experience/{id}
     * Delete a work experience entry
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkExperience(
            Authentication authentication,
            @PathVariable Long id) {
        String email = authentication.getName();
        workExperienceService.deleteWorkExperience(email, id);
        return ResponseEntity.noContent().build();
    }
}
