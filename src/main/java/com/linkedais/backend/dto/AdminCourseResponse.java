package com.linkedais.backend.dto;

public class AdminCourseResponse {
    private Long id;
    private String name;
    private String code;
    private int credits;
    private String semester;
    private String lecturer;
    private boolean active;

    public AdminCourseResponse() {}

    public AdminCourseResponse(Long id, String name, String code, int credits,
                                String semester, String lecturer, boolean active) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.credits = credits;
        this.semester = semester;
        this.lecturer = lecturer;
        this.active = active;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public String getLecturer() { return lecturer; }
    public void setLecturer(String lecturer) { this.lecturer = lecturer; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
