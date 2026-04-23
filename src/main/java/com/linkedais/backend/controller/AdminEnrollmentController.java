package com.linkedais.backend.controller;

import com.linkedais.backend.dto.EnrollmentRequest;
import com.linkedais.backend.dto.EnrollmentResponse;
import com.linkedais.backend.dto.GradeRequest;
import com.linkedais.backend.service.EnrollmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminEnrollmentController {

    private final EnrollmentService enrollmentService;

    public AdminEnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping("/enrollments")
    public ResponseEntity<EnrollmentResponse> enroll(@RequestBody EnrollmentRequest request) {
        return ResponseEntity.ok(enrollmentService.enrollStudent(request));
    }

    @DeleteMapping("/enrollments/{id}")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable Long id) {
        enrollmentService.deleteEnrollment(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/grades/{enrollmentId}")
    public ResponseEntity<EnrollmentResponse> updateGrade(@PathVariable Long enrollmentId,
                                                           @RequestBody GradeRequest request) {
        return ResponseEntity.ok(enrollmentService.updateGrade(enrollmentId, request.getGrade()));
    }
}
