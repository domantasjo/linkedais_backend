package com.linkedais.backend.service;

import com.linkedais.backend.dto.EducationRequest;
import com.linkedais.backend.dto.EducationResponse;
import com.linkedais.backend.model.Education;
import com.linkedais.backend.model.User;
import com.linkedais.backend.repository.EducationRepository;
import com.linkedais.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EducationService {

    @Autowired
    private EducationRepository educationRepository;

    @Autowired
    private UserRepository userRepository;

    public List<EducationResponse> getEducationByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return getEducationByUserId(user.getId());
    }

    public List<EducationResponse> getEducationByUserId(Long userId) {
        return educationRepository.findByUserIdOrderByStartYearDesc(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public EducationResponse addEducation(String email, EducationRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Education education = new Education();
        education.setUser(user);
        education.setInstitution(request.getInstitution());
        education.setDegree(request.getDegree());
        education.setFieldOfStudy(request.getFieldOfStudy());
        education.setStartYear(request.getStartYear());
        education.setEndYear(request.getEndYear());

        return toResponse(educationRepository.save(education));
    }

    public EducationResponse updateEducation(String email, Long id, EducationRequest request) {
        Education education = educationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Education not found"));

        if (!education.getUser().getEmail().equals(email)) {
            throw new RuntimeException("You can only update your own education entries");
        }

        education.setInstitution(request.getInstitution());
        education.setDegree(request.getDegree());
        education.setFieldOfStudy(request.getFieldOfStudy());
        education.setStartYear(request.getStartYear());
        education.setEndYear(request.getEndYear());

        return toResponse(educationRepository.save(education));
    }

    public void deleteEducation(String email, Long id) {
        Education education = educationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Education not found"));

        if (!education.getUser().getEmail().equals(email)) {
            throw new RuntimeException("You can only delete your own education entries");
        }

        educationRepository.deleteById(id);
    }

    private EducationResponse toResponse(Education education) {
        EducationResponse response = new EducationResponse();
        response.setId(education.getId());
        response.setInstitution(education.getInstitution());
        response.setDegree(education.getDegree());
        response.setFieldOfStudy(education.getFieldOfStudy());
        response.setStartYear(education.getStartYear());
        response.setEndYear(education.getEndYear());
        return response;
    }
}