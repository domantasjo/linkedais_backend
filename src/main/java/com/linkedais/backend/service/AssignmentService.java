package com.linkedais.backend.service;

import com.linkedais.backend.dto.AssignmentRequest;
import com.linkedais.backend.dto.AssignmentResponse;
import com.linkedais.backend.model.Assignment;
import com.linkedais.backend.model.AssignmentGrade;
import com.linkedais.backend.model.Course;
import com.linkedais.backend.model.Enrollment;
import com.linkedais.backend.repository.AssignmentGradeRepository;
import com.linkedais.backend.repository.AssignmentRepository;
import com.linkedais.backend.repository.CourseRepository;
import com.linkedais.backend.repository.EnrollmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final AssignmentGradeRepository assignmentGradeRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    public AssignmentService(AssignmentRepository assignmentRepository,
                              AssignmentGradeRepository assignmentGradeRepository,
                              CourseRepository courseRepository,
                              EnrollmentRepository enrollmentRepository) {
        this.assignmentRepository = assignmentRepository;
        this.assignmentGradeRepository = assignmentGradeRepository;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    public List<AssignmentResponse> listByCourse(Long courseId) {
        return assignmentRepository.findByCourseIdOrderByWeekAscIdAsc(courseId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public AssignmentResponse create(Long courseId, AssignmentRequest req) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        validate(req, true);
        Assignment a = new Assignment();
        a.setCourse(course);
        a.setCode(req.getCode());
        a.setName(req.getName());
        a.setWeight(req.getWeight());
        a.setWeek(req.getWeek());
        a.setMaxScore(req.getMaxScore() != null ? req.getMaxScore() : 10.0);
        return toResponse(assignmentRepository.save(a));
    }

    public AssignmentResponse update(Long id, AssignmentRequest req) {
        Assignment a = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
        validate(req, false);
        if (req.getCode() != null) a.setCode(req.getCode());
        if (req.getName() != null) a.setName(req.getName());
        if (req.getWeight() != null) a.setWeight(req.getWeight());
        if (req.getWeek() != null) a.setWeek(req.getWeek());
        if (req.getMaxScore() != null) a.setMaxScore(req.getMaxScore());
        return toResponse(assignmentRepository.save(a));
    }

    @Transactional
    public void delete(Long id) {
        if (!assignmentRepository.existsById(id)) {
            throw new RuntimeException("Assignment not found");
        }
        assignmentGradeRepository.deleteByAssignmentId(id);
        assignmentRepository.deleteById(id);
    }

    /** Upsert or clear a score for an enrollment+assignment pair. */
    public void setScore(Long enrollmentId, Long assignmentId, Double score) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
        if (!assignment.getCourse().getId().equals(enrollment.getCourse().getId())) {
            throw new RuntimeException("Assignment does not belong to the enrollment's course");
        }
        if (score == null) {
            assignmentGradeRepository
                    .findByEnrollmentIdAndAssignmentId(enrollmentId, assignmentId)
                    .ifPresent(assignmentGradeRepository::delete);
            return;
        }
        if (score < 0 || score > assignment.getMaxScore()) {
            throw new RuntimeException("Score must be between 0 and " + assignment.getMaxScore());
        }
        AssignmentGrade ag = assignmentGradeRepository
                .findByEnrollmentIdAndAssignmentId(enrollmentId, assignmentId)
                .orElseGet(() -> {
                    AssignmentGrade n = new AssignmentGrade();
                    n.setEnrollment(enrollment);
                    n.setAssignment(assignment);
                    return n;
                });
        ag.setScore(score);
        assignmentGradeRepository.save(ag);
    }

    private void validate(AssignmentRequest req, boolean creating) {
        if (creating) {
            if (req.getCode() == null || req.getCode().isBlank()) throw new RuntimeException("code required");
            if (req.getName() == null || req.getName().isBlank()) throw new RuntimeException("name required");
            if (req.getWeight() == null) throw new RuntimeException("weight required");
        }
        if (req.getWeight() != null && (req.getWeight() < 0 || req.getWeight() > 100)) {
            throw new RuntimeException("weight must be 0..100");
        }
        if (req.getMaxScore() != null && req.getMaxScore() <= 0) {
            throw new RuntimeException("maxScore must be > 0");
        }
    }

    public AssignmentResponse toResponse(Assignment a) {
        return new AssignmentResponse(a.getId(), a.getCode(), a.getName(),
                a.getWeight(), a.getWeek(), a.getMaxScore());
    }
}
