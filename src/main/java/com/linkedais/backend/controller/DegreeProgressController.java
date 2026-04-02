package com.linkedais.backend.controller;

import com.linkedais.backend.service.DegreeProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/degree-progress")
public class DegreeProgressController {

    @Autowired
    private DegreeProgressService degreeProgressService;

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getMyDegreeProgress(Authentication authentication) {
        String email = authentication.getName();
        Map<String, Object> progress = degreeProgressService.getDegreeProgressByEmail(email);
        return ResponseEntity.ok(progress);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Map<String, Object>> getUserDegreeProgress(@PathVariable Long userId) {
        Map<String, Object> progress = degreeProgressService.getDegreeProgressByUserId(userId);
        return ResponseEntity.ok(progress);
    }
}