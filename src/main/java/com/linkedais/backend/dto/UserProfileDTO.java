package com.linkedais.backend.dto;

import java.util.List;

public record UserProfileDTO(
        Long id,
        String name,
        String email,
        String bio,
        String headline,
        String university,
        String studyProgram,
        List<String> skills,
        List<CourseDTO> courses
) {}
