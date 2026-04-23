package com.linkedais.backend.service;

import com.linkedais.backend.dto.AdminCourseRequest;
import com.linkedais.backend.dto.AdminCourseResponse;
import com.linkedais.backend.model.Course;
import com.linkedais.backend.repository.CourseRepository;
import com.linkedais.backend.repository.EnrollmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    public CourseService(CourseRepository courseRepository, EnrollmentRepository enrollmentRepository) {
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    public List<AdminCourseResponse> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<AdminCourseResponse> getActiveCourses() {
        return courseRepository.findByActiveTrue().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public AdminCourseResponse getCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return toResponse(course);
    }

    public AdminCourseResponse createCourse(AdminCourseRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new RuntimeException("Course name is required");
        }
        if (request.getCredits() == null || request.getCredits() < 0) {
            throw new RuntimeException("Valid credits value is required");
        }
        Course course = new Course();
        course.setName(request.getName());
        course.setCode(request.getCode());
        course.setCredits(request.getCredits());
        course.setSemester(request.getSemester());
        course.setInstructor(request.getLecturer() != null ? request.getLecturer() : "");
        course.setActive(request.getActive() == null || request.getActive());
        return toResponse(courseRepository.save(course));
    }

    public AdminCourseResponse updateCourse(Long id, AdminCourseRequest request) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        if (request.getName() != null) course.setName(request.getName());
        if (request.getCode() != null) course.setCode(request.getCode());
        if (request.getCredits() != null) course.setCredits(request.getCredits());
        if (request.getSemester() != null) course.setSemester(request.getSemester());
        if (request.getLecturer() != null) course.setInstructor(request.getLecturer());
        if (request.getActive() != null) course.setActive(request.getActive());
        return toResponse(courseRepository.save(course));
    }

    @Transactional
    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        // Remove enrollments first to respect FK constraints
        enrollmentRepository.findByCourseId(id).forEach(enrollmentRepository::delete);
        courseRepository.delete(course);
    }

    public AdminCourseResponse toResponse(Course c) {
        return new AdminCourseResponse(
                c.getId(),
                c.getName(),
                c.getCode(),
                c.getCredits(),
                c.getSemester(),
                c.getInstructor(),
                c.isActive()
        );
    }
}
