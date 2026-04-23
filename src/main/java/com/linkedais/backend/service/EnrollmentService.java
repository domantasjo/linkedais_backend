package com.linkedais.backend.service;

import com.linkedais.backend.dto.EnrollmentRequest;
import com.linkedais.backend.dto.EnrollmentResponse;
import com.linkedais.backend.model.Course;
import com.linkedais.backend.model.Enrollment;
import com.linkedais.backend.model.User;
import com.linkedais.backend.repository.CourseRepository;
import com.linkedais.backend.repository.EnrollmentRepository;
import com.linkedais.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    public EnrollmentService(EnrollmentRepository enrollmentRepository,
                              UserRepository userRepository,
                              CourseRepository courseRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    public EnrollmentResponse enrollStudent(EnrollmentRequest request) {
        if (request.getStudentId() == null || request.getCourseId() == null) {
            throw new RuntimeException("studentId and courseId are required");
        }
        User student = userRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        enrollmentRepository.findByStudentIdAndCourseId(student.getId(), course.getId())
                .ifPresent(e -> {
                    throw new RuntimeException("Student already enrolled in this course");
                });

        Enrollment enrollment = new Enrollment(student, course);
        return toResponse(enrollmentRepository.save(enrollment));
    }

    public EnrollmentResponse updateGrade(Long enrollmentId, Double grade) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));
        if (grade != null && (grade < 0 || grade > 10)) {
            throw new RuntimeException("Grade must be between 0 and 10");
        }
        enrollment.setGrade(grade);
        return toResponse(enrollmentRepository.save(enrollment));
    }

    public List<EnrollmentResponse> getCourseRoster(Long courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new RuntimeException("Course not found");
        }
        return enrollmentRepository.findByCourseId(courseId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<EnrollmentResponse> getStudentEnrollments(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<EnrollmentResponse> getStudentEnrollmentsByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return getStudentEnrollments(user.getId());
    }

    public void deleteEnrollment(Long enrollmentId) {
        if (!enrollmentRepository.existsById(enrollmentId)) {
            throw new RuntimeException("Enrollment not found");
        }
        enrollmentRepository.deleteById(enrollmentId);
    }

    private EnrollmentResponse toResponse(Enrollment e) {
        User student = e.getStudent();
        Course course = e.getCourse();
        return new EnrollmentResponse(
                e.getId(),
                student.getId(),
                student.getName(),
                student.getEmail(),
                course.getId(),
                course.getName(),
                course.getCode(),
                e.getGrade(),
                e.getEnrolledAt()
        );
    }
}
