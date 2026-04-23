package com.linkedais.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "assignment_grades", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"enrollment_id", "assignment_id"})
})
public class AssignmentGrade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id", nullable = false)
    private Enrollment enrollment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;

    /** Score on the same scale as assignment.maxScore */
    @Column(nullable = false)
    private double score;

    @Column(name = "graded_at", nullable = false)
    private LocalDateTime gradedAt;

    @PrePersist
    @PreUpdate
    protected void touch() {
        this.gradedAt = LocalDateTime.now();
    }

    public AssignmentGrade() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Enrollment getEnrollment() { return enrollment; }
    public void setEnrollment(Enrollment enrollment) { this.enrollment = enrollment; }

    public Assignment getAssignment() { return assignment; }
    public void setAssignment(Assignment assignment) { this.assignment = assignment; }

    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }

    public LocalDateTime getGradedAt() { return gradedAt; }
    public void setGradedAt(LocalDateTime gradedAt) { this.gradedAt = gradedAt; }
}
