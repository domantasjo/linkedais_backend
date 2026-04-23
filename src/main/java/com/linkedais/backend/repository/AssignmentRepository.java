package com.linkedais.backend.repository;

import com.linkedais.backend.model.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findByCourseIdOrderByWeekAscIdAsc(Long courseId);
}
