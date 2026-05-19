package com.linkedais.backend.controller;

import com.linkedais.backend.dto.EducationRequest;
import com.linkedais.backend.dto.EducationResponse;
import com.linkedais.backend.service.EducationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/education")
public class EducationController {

    @Autowired
    private EducationService educationService;

    @GetMapping("/me")
    public ResponseEntity<List<EducationResponse>> getMyEducation(Authentication auth) {
        String email = auth.getName();
        return ResponseEntity.ok(educationService.getEducationByEmail(email));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<EducationResponse>> getUserEducation(@PathVariable Long userId) {
        return ResponseEntity.ok(educationService.getEducationByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<EducationResponse> addEducation(Authentication auth, @RequestBody EducationRequest request) {
        String email = auth.getName();
        return ResponseEntity.ok(educationService.addEducation(email, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EducationResponse> updateEducation(Authentication auth, @PathVariable Long id, @RequestBody EducationRequest request) {
        String email = auth.getName();
        return ResponseEntity.ok(educationService.updateEducation(email, id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEducation(Authentication auth, @PathVariable Long id) {
        String email = auth.getName();
        educationService.deleteEducation(email, id);
        return ResponseEntity.noContent().build();
    }
}