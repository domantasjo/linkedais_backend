package com.linkedais.backend.service;

import com.linkedais.backend.dto.WorkExperienceRequest;
import com.linkedais.backend.dto.WorkExperienceResponse;
import com.linkedais.backend.model.User;
import com.linkedais.backend.model.WorkExperience;
import com.linkedais.backend.repository.UserRepository;
import com.linkedais.backend.repository.WorkExperienceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkExperienceService {

    private final WorkExperienceRepository workExperienceRepository;
    private final UserRepository userRepository;

    public WorkExperienceService(WorkExperienceRepository workExperienceRepository, UserRepository userRepository) {
        this.workExperienceRepository = workExperienceRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public WorkExperienceResponse addWorkExperience(String userEmail, WorkExperienceRequest request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        WorkExperience experience = new WorkExperience(
                user,
                request.company(),
                request.role(),
                request.startDate(),
                request.endDate(),
                request.description()
        );

        WorkExperience saved = workExperienceRepository.save(experience);
        return toResponse(saved);
    }

    @Transactional
    public WorkExperienceResponse updateWorkExperience(String userEmail, Long experienceId, WorkExperienceRequest request) {
        WorkExperience experience = workExperienceRepository.findById(experienceId)
                .orElseThrow(() -> new RuntimeException("Work experience not found"));

        // Verify ownership
        if (!experience.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Unauthorized to update this work experience");
        }

        experience.setCompany(request.company());
        experience.setRole(request.role());
        experience.setStartDate(request.startDate());
        experience.setEndDate(request.endDate());
        experience.setDescription(request.description());

        WorkExperience updated = workExperienceRepository.save(experience);
        return toResponse(updated);
    }

    @Transactional
    public void deleteWorkExperience(String userEmail, Long experienceId) {
        WorkExperience experience = workExperienceRepository.findById(experienceId)
                .orElseThrow(() -> new RuntimeException("Work experience not found"));

        // Verify ownership
        if (!experience.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Unauthorized to delete this work experience");
        }

        workExperienceRepository.delete(experience);
    }

    public List<WorkExperienceResponse> getUserWorkExperiences(Long userId) {
        return workExperienceRepository.findByUserIdOrderByStartDateDesc(userId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private WorkExperienceResponse toResponse(WorkExperience experience) {
        return new WorkExperienceResponse(
                experience.getId(),
                experience.getCompany(),
                experience.getRole(),
                experience.getStartDate(),
                experience.getEndDate(),
                experience.getDescription()
        );
    }
}
