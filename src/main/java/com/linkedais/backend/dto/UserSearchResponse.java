package com.linkedais.backend.dto;

public class UserSearchResponse {
    private Long id;
    private String name;
    private String studyProgram;

    public UserSearchResponse() {}

    public UserSearchResponse(Long id, String name, String studyProgram) {
        this.id = id;
        this.name = name;
        this.studyProgram = studyProgram;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getStudyProgram() { return studyProgram; }
    public void setStudyProgram(String studyProgram) { this.studyProgram = studyProgram; }
}
