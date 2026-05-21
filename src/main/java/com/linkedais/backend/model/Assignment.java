package com.linkedais.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "assignments")
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    /** Short code like "IR", "LB", "TE", "E1" */
    @Column(nullable = false, length = 20)
    private String code;

    @Column(nullable = false)
    private String name;

    /** Percentage weight, 0-100 */
    @Column(nullable = false)
    private int weight;

    /** Optional week number (e.g. 9 for midterm week) */
    @Column
    private Integer week;

    /** Max score (default 10 — Lithuanian 10-point scale) */
    @Column(nullable = false)
    private double maxScore = 10.0;

    public Assignment() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getWeight() { return weight; }
    public void setWeight(int weight) { this.weight = weight; }

    public Integer getWeek() { return week; }
    public void setWeek(Integer week) { this.week = week; }

    public double getMaxScore() { return maxScore; }
    public void setMaxScore(double maxScore) { this.maxScore = maxScore; }
}
