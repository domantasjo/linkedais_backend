package com.linkedais.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String instructor;

    @Column(nullable = false)
    private int credits;

    @Column(length = 50)
    private String code;

    @Column(length = 50)
    private String semester;

    @Column(nullable = false)
    private boolean active = true;

    // Constructors
    public Course() {}

    public Course(String name, String instructor) {
        this.name = name;
        this.instructor = instructor;
    }


    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getInstructor() { return instructor; }
    public void setInstructor(String instructor) { this.instructor = instructor; }

    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}