package com.linkedais.backend.dto;

public class AssignmentScoreDTO {
    private Long assignmentId;
    private String code;
    private String name;
    private int weight;
    private Integer week;
    private double maxScore;
    /** null if not graded */
    private Double score;

    public AssignmentScoreDTO() {}

    public AssignmentScoreDTO(Long assignmentId, String code, String name, int weight,
                               Integer week, double maxScore, Double score) {
        this.assignmentId = assignmentId;
        this.code = code;
        this.name = name;
        this.weight = weight;
        this.week = week;
        this.maxScore = maxScore;
        this.score = score;
    }

    public Long getAssignmentId() { return assignmentId; }
    public void setAssignmentId(Long assignmentId) { this.assignmentId = assignmentId; }

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

    public Double getScore() { return score; }
    public void setScore(Double score) { this.score = score; }
}
