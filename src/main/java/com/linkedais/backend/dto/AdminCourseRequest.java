package com.linkedais.backend.dto;

public class AdminCourseRequest {
    private String name;
    private String code;
    private Integer credits;
    private String semester;
    private String lecturer;
    private Boolean active;

    public AdminCourseRequest() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public Integer getCredits() { return credits; }
    public void setCredits(Integer credits) { this.credits = credits; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public String getLecturer() { return lecturer; }
    public void setLecturer(String lecturer) { this.lecturer = lecturer; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}
