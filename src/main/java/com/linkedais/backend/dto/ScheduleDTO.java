package com.linkedais.backend.dto;

import java.time.LocalTime;
import java.time.LocalDate;

public record ScheduleDTO(
        Long id,
        Long courseId,
        String courseName,
        String dayOfWeek,
        LocalTime startTime,
        LocalTime endTime,
        String location,
        String type,
        String recurrence,      // WEEKLY, BIWEEKLY, ONCE
        LocalDate specificDate  // Only for ONCE events
) {}