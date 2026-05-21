package com.linkedais.backend.service;

import com.linkedais.backend.dto.TranscriptDTO;
import com.linkedais.backend.model.Course;
import com.linkedais.backend.model.Enrollment;
import com.linkedais.backend.model.User;
import com.linkedais.backend.repository.EnrollmentRepository;
import com.linkedais.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class TranscriptService {

    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;

    public TranscriptService(EnrollmentRepository enrollmentRepository, UserRepository userRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.userRepository = userRepository;
    }

    public TranscriptDTO getTranscriptForEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return buildTranscript(user);
    }

    private TranscriptDTO buildTranscript(User user) {
        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(user.getId());

        // Group by semester preserving insertion order (sorted by semester label)
        Map<String, List<Enrollment>> bySemester = new LinkedHashMap<>();
        enrollments.stream()
                .sorted(Comparator.comparing((Enrollment e) -> {
                    String s = e.getCourse().getSemester();
                    return s == null ? "" : s;
                }).thenComparing(e -> {
                    String n = e.getCourse().getName();
                    return n == null ? "" : n;
                }))
                .forEach(e -> {
                    String sem = e.getCourse().getSemester();
                    if (sem == null || sem.isBlank()) sem = "Nepriskirta";
                    bySemester.computeIfAbsent(sem, k -> new ArrayList<>()).add(e);
                });

        List<TranscriptDTO.SemesterGroup> semesters = new ArrayList<>();
        int totalCreditsAll = 0;
        int earnedCreditsAll = 0;
        double weightedSumAll = 0.0;
        int gradedCreditsAll = 0;

        for (Map.Entry<String, List<Enrollment>> entry : bySemester.entrySet()) {
            List<TranscriptDTO.CourseEntry> courseEntries = new ArrayList<>();
            int semTotal = 0;
            int semEarned = 0;
            double semWeighted = 0.0;
            int semGraded = 0;

            for (Enrollment e : entry.getValue()) {
                Course c = e.getCourse();
                int credits = c.getCredits();
                semTotal += credits;
                if (e.getGrade() != null) {
                    semWeighted += e.getGrade() * credits;
                    semGraded += credits;
                    if (e.getGrade() >= 5.0) {
                        semEarned += credits;
                    }
                }
                courseEntries.add(new TranscriptDTO.CourseEntry(
                        e.getId(),
                        c.getId(),
                        c.getCode(),
                        c.getName(),
                        c.getInstructor(),
                        credits,
                        e.getGrade()
                ));
            }

            Double semGpa = semGraded > 0 ? semWeighted / semGraded : null;
            semesters.add(new TranscriptDTO.SemesterGroup(
                    entry.getKey(), courseEntries, semTotal, semEarned, semGpa
            ));

            totalCreditsAll += semTotal;
            earnedCreditsAll += semEarned;
            weightedSumAll += semWeighted;
            gradedCreditsAll += semGraded;
        }

        Double gpa = gradedCreditsAll > 0 ? weightedSumAll / gradedCreditsAll : null;

        return new TranscriptDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                semesters,
                totalCreditsAll,
                earnedCreditsAll,
                gpa
        );
    }
}
