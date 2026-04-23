package com.linkedais.backend.dto;

public class AssignmentRequest {
    private String code;
    private String name;
    private Integer weight;
    private Integer week;
    private Double maxScore;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getWeight() { return weight; }
    public void setWeight(Integer weight) { this.weight = weight; }

    public Integer getWeek() { return week; }
    public void setWeek(Integer week) { this.week = week; }

    public Double getMaxScore() { return maxScore; }
    public void setMaxScore(Double maxScore) { this.maxScore = maxScore; }
}
