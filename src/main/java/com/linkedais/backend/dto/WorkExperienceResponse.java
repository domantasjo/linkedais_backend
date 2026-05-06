package com.linkedais.backend.dto;

import java.time.LocalDate;

public record WorkExperienceResponse(
        Long id,
        String company,
        String role,
        LocalDate startDate,
        LocalDate endDate,
        String description
) {}
