package com.linkedais.backend.controller;

import com.linkedais.backend.dto.AssignmentRequest;
import com.linkedais.backend.dto.AssignmentResponse;
import com.linkedais.backend.dto.ScoreRequest;
import com.linkedais.backend.service.AssignmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminAssignmentController {

    private final AssignmentService assignmentService;

    public AdminAssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @GetMapping("/courses/{courseId}/assignments")
    public ResponseEntity<List<AssignmentResponse>> list(@PathVariable Long courseId) {
        return ResponseEntity.ok(assignmentService.listByCourse(courseId));
    }

    @PostMapping("/courses/{courseId}/assignments")
    public ResponseEntity<AssignmentResponse> create(@PathVariable Long courseId,
                                                      @RequestBody AssignmentRequest req) {
        return ResponseEntity.ok(assignmentService.create(courseId, req));
    }

    @PutMapping("/assignments/{id}")
    public ResponseEntity<AssignmentResponse> update(@PathVariable Long id,
                                                      @RequestBody AssignmentRequest req) {
        return ResponseEntity.ok(assignmentService.update(id, req));
    }

    @DeleteMapping("/assignments/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        assignmentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /** Set score for one student's assignment. Score null to clear. */
    @PutMapping("/grades/{enrollmentId}/assignments/{assignmentId}")
    public ResponseEntity<Void> setScore(@PathVariable Long enrollmentId,
                                          @PathVariable Long assignmentId,
                                          @RequestBody ScoreRequest req) {
        assignmentService.setScore(enrollmentId, assignmentId, req.getScore());
        return ResponseEntity.ok().build();
    }
}
