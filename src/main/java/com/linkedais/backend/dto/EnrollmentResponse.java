package com.linkedais.backend.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentResponse {
    private Long id;
    private Long studentId;
    private String studentName;
    private String studentEmail;
    private Long courseId;
    private String courseName;
    private String courseCode;
    /** Admin-set final grade override (may be null). */
    private Double grade;
    /** Auto-computed weighted grade (may be null if no assignments graded). */
    private Double suggestedGrade;
    private List<AssignmentScoreDTO> assignmentScores = new ArrayList<>();
    private LocalDateTime enrolledAt;

    public EnrollmentResponse() {}

    public EnrollmentResponse(Long id, Long studentId, String studentName, String studentEmail,
                               Long courseId, String courseName, String courseCode,
                               Double grade, LocalDateTime enrolledAt) {
        this.id = id;
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.grade = grade;
        this.enrolledAt = enrolledAt;
    }

    public Double getSuggestedGrade() { return suggestedGrade; }
    public void setSuggestedGrade(Double suggestedGrade) { this.suggestedGrade = suggestedGrade; }

    public List<AssignmentScoreDTO> getAssignmentScores() { return assignmentScores; }
    public void setAssignmentScores(List<AssignmentScoreDTO> assignmentScores) { this.assignmentScores = assignmentScores; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getStudentEmail() { return studentEmail; }
    public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }

    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public Double getGrade() { return grade; }
    public void setGrade(Double grade) { this.grade = grade; }

    public LocalDateTime getEnrolledAt() { return enrolledAt; }
    public void setEnrolledAt(LocalDateTime enrolledAt) { this.enrolledAt = enrolledAt; }
}
