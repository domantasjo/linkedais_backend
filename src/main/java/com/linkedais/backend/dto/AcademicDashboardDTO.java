package com.linkedais.backend.dto;

import java.util.List;

public record AcademicDashboardDTO(
    List<CourseDTO> courses,
    Integer degreeProgress,
    String upcomingSchedule,
    String academicRecordSummary
) {}
