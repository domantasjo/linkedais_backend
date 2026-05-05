package com.linkedais.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public class UpdateProfileRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 2, message = "Name must be at least 2 characters")
    private String name;

    @Size(max = 500, message = "Bio must be at most 500 characters")
    private String bio;

    @Size(max = 120, message = "Headline must be at most 120 characters")
    private String headline;

    @Size(max = 200, message = "University must be at most 200 characters")
    private String university;

    @Size(max = 200, message = "Study program must be at most 200 characters")
    private String studyProgram;

    @Size(max = 20, message = "Skills list can have at most 20 items")
    private List<@Size(max = 50, message = "Each skill must be at most 50 characters") String> skills;

    public UpdateProfileRequest() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getHeadline() { return headline; }
    public void setHeadline(String headline) { this.headline = headline; }

    public String getUniversity() { return university; }
    public void setUniversity(String university) { this.university = university; }

    public String getStudyProgram() { return studyProgram; }
    public void setStudyProgram(String studyProgram) { this.studyProgram = studyProgram; }

    public List<String> getSkills() { return skills; }
    public void setSkills(List<String> skills) { this.skills = skills; }
}
