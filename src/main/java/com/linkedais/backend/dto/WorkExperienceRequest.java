package com.linkedais.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record WorkExperienceRequest(
        @NotBlank(message = "Company name is required")
        @Size(max = 200, message = "Company name must not exceed 200 characters")
        String company,

        @NotBlank(message = "Role is required")
        @Size(max = 200, message = "Role must not exceed 200 characters")
        String role,

        @NotNull(message = "Start date is required")
        LocalDate startDate,

        LocalDate endDate,

        @Size(max = 2000, message = "Description must not exceed 2000 characters")
        String description
) {}
