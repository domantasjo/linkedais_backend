package com.linkedais.backend.controller;

import com.linkedais.backend.dto.AdminCourseRequest;
import com.linkedais.backend.dto.AdminCourseResponse;
import com.linkedais.backend.dto.EnrollmentResponse;
import com.linkedais.backend.service.CourseService;
import com.linkedais.backend.service.EnrollmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Admin Course Controller
 * All endpoints protected by admin role guard (see SecurityConfig: /api/admin/** hasAuthority("ADMIN"))
 */
@RestController
@RequestMapping("/api/admin/courses")
public class AdminCourseController {

    private final CourseService courseService;
    private final EnrollmentService enrollmentService;

    public AdminCourseController(CourseService courseService, EnrollmentService enrollmentService) {
        this.courseService = courseService;
        this.enrollmentService = enrollmentService;
    }

    @GetMapping
    public ResponseEntity<List<AdminCourseResponse>> listCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminCourseResponse> getCourse(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCourse(id));
    }

    @PostMapping
    public ResponseEntity<AdminCourseResponse> createCourse(@RequestBody AdminCourseRequest request) {
        return ResponseEntity.ok(courseService.createCourse(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminCourseResponse> updateCourse(@PathVariable Long id,
                                                             @RequestBody AdminCourseRequest request) {
        return ResponseEntity.ok(courseService.updateCourse(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Roster: students enrolled in this course.
     */
    @GetMapping("/{id}/students")
    public ResponseEntity<List<EnrollmentResponse>> getRoster(@PathVariable Long id) {
        return ResponseEntity.ok(enrollmentService.getCourseRoster(id));
    }
}
