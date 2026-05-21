package com.linkedais.backend.dto;

import java.util.List;

public class TranscriptDTO {

    public static class CourseEntry {
        private Long enrollmentId;
        private Long courseId;
        private String courseCode;
        private String courseName;
        private String instructor;
        private int credits;
        private Double grade;

        public CourseEntry() {}

        public CourseEntry(Long enrollmentId, Long courseId, String courseCode, String courseName,
                           String instructor, int credits, Double grade) {
            this.enrollmentId = enrollmentId;
            this.courseId = courseId;
            this.courseCode = courseCode;
            this.courseName = courseName;
            this.instructor = instructor;
            this.credits = credits;
            this.grade = grade;
        }

        public Long getEnrollmentId() { return enrollmentId; }
        public void setEnrollmentId(Long enrollmentId) { this.enrollmentId = enrollmentId; }
        public Long getCourseId() { return courseId; }
        public void setCourseId(Long courseId) { this.courseId = courseId; }
        public String getCourseCode() { return courseCode; }
        public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
        public String getCourseName() { return courseName; }
        public void setCourseName(String courseName) { this.courseName = courseName; }
        public String getInstructor() { return instructor; }
        public void setInstructor(String instructor) { this.instructor = instructor; }
        public int getCredits() { return credits; }
        public void setCredits(int credits) { this.credits = credits; }
        public Double getGrade() { return grade; }
        public void setGrade(Double grade) { this.grade = grade; }
    }

    public static class SemesterGroup {
        private String semester;
        private List<CourseEntry> courses;
        private int totalCredits;
        private int earnedCredits;
        private Double semesterGpa;

        public SemesterGroup() {}

        public SemesterGroup(String semester, List<CourseEntry> courses,
                             int totalCredits, int earnedCredits, Double semesterGpa) {
            this.semester = semester;
            this.courses = courses;
            this.totalCredits = totalCredits;
            this.earnedCredits = earnedCredits;
            this.semesterGpa = semesterGpa;
        }

        public String getSemester() { return semester; }
        public void setSemester(String semester) { this.semester = semester; }
        public List<CourseEntry> getCourses() { return courses; }
        public void setCourses(List<CourseEntry> courses) { this.courses = courses; }
        public int getTotalCredits() { return totalCredits; }
        public void setTotalCredits(int totalCredits) { this.totalCredits = totalCredits; }
        public int getEarnedCredits() { return earnedCredits; }
        public void setEarnedCredits(int earnedCredits) { this.earnedCredits = earnedCredits; }
        public Double getSemesterGpa() { return semesterGpa; }
        public void setSemesterGpa(Double semesterGpa) { this.semesterGpa = semesterGpa; }
    }

    private Long studentId;
    private String studentName;
    private String studentEmail;
    private List<SemesterGroup> semesters;
    private int totalCredits;
    private int earnedCredits;
    private Double gpa;

    public TranscriptDTO() {}

    public TranscriptDTO(Long studentId, String studentName, String studentEmail,
                         List<SemesterGroup> semesters, int totalCredits, int earnedCredits, Double gpa) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.semesters = semesters;
        this.totalCredits = totalCredits;
        this.earnedCredits = earnedCredits;
        this.gpa = gpa;
    }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public String getStudentEmail() { return studentEmail; }
    public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }
    public List<SemesterGroup> getSemesters() { return semesters; }
    public void setSemesters(List<SemesterGroup> semesters) { this.semesters = semesters; }
    public int getTotalCredits() { return totalCredits; }
    public void setTotalCredits(int totalCredits) { this.totalCredits = totalCredits; }
    public int getEarnedCredits() { return earnedCredits; }
    public void setEarnedCredits(int earnedCredits) { this.earnedCredits = earnedCredits; }
    public Double getGpa() { return gpa; }
    public void setGpa(Double gpa) { this.gpa = gpa; }
}
