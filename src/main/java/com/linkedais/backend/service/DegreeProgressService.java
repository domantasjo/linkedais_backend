package com.linkedais.backend.service;

import com.linkedais.backend.dto.EnrollmentResponse;
import com.linkedais.backend.model.Course;
import com.linkedais.backend.model.Enrollment;
import com.linkedais.backend.model.User;
import com.linkedais.backend.repository.EnrollmentRepository;
import com.linkedais.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DegreeProgressService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private EnrollmentService enrollmentService;

    /** Passing grade threshold on the 10-point Lithuanian scale. */
    private static final double PASSING_GRADE = 5.0;

    public Map<String, Object> getDegreeProgressByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return calculateProgress(user);
    }

    public Map<String, Object> getDegreeProgressByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return calculateProgress(user);
    }

    private Map<String, Object> calculateProgress(User user) {
        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(user.getId());

        int totalCredits = 0;
        int completedCredits = 0;

        for (Enrollment e : enrollments) {
            Course course = e.getCourse();
            totalCredits += course.getCredits();

            // Prefer admin-set final grade; fall back to computed suggested grade.
            Double finalGrade = e.getGrade();
            if (finalGrade == null) {
                EnrollmentResponse enriched = enrollmentService.getStudentEnrollments(user.getId()).stream()
                        .filter(r -> r.getId().equals(e.getId()))
                        .findFirst()
                        .orElse(null);
                if (enriched != null) {
                    finalGrade = enriched.getSuggestedGrade();
                }
            }
            if (finalGrade != null && finalGrade >= PASSING_GRADE) {
                completedCredits += course.getCredits();
            }
        }

        int remainingCredits = totalCredits - completedCredits;
        double percentage = totalCredits == 0 ? 0 :
                (double) completedCredits / totalCredits * 100;

        Map<String, Object> result = new HashMap<>();
        result.put("completedCredits", completedCredits);
        result.put("totalCredits", totalCredits);
        result.put("remainingCredits", remainingCredits);
        result.put("percentage", percentage);
        return result;
    }
}
