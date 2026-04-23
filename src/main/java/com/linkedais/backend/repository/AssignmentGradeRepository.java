package com.linkedais.backend.repository;

import com.linkedais.backend.model.AssignmentGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssignmentGradeRepository extends JpaRepository<AssignmentGrade, Long> {
    List<AssignmentGrade> findByEnrollmentId(Long enrollmentId);
    Optional<AssignmentGrade> findByEnrollmentIdAndAssignmentId(Long enrollmentId, Long assignmentId);
    void deleteByAssignmentId(Long assignmentId);
}
