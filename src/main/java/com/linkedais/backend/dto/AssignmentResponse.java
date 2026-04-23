package com.linkedais.backend.dto;

public class AssignmentResponse {
    private Long id;
    private String code;
    private String name;
    private int weight;
    private Integer week;
    private double maxScore;

    public AssignmentResponse() {}

    public AssignmentResponse(Long id, String code, String name, int weight, Integer week, double maxScore) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.weight = weight;
        this.week = week;
        this.maxScore = maxScore;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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
