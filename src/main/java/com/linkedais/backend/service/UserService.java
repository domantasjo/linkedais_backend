package com.linkedais.backend.service;

import com.linkedais.backend.dto.UpdateProfileRequest;
import com.linkedais.backend.dto.UserProfileDTO;
import com.linkedais.backend.model.User;
import com.linkedais.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserProfileDTO getPublicProfile(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return toProfileDTO(user);
    }

    public UserProfileDTO getMyProfile(String email) {
        User user = findByEmail(email);
        return toProfileDTO(user);
    }

    public UserProfileDTO updateProfile(String email, UpdateProfileRequest request) {
        User user = findByEmail(email);

        user.setName(request.getName());
        user.setBio(request.getBio());
        user.setUniversity(request.getUniversity());
        user.setStudyProgram(request.getStudyProgram());
        if (request.getSkills() != null) {
            user.setSkills(request.getSkills());
        }

        User updated = userRepository.save(user);
        return toProfileDTO(updated);
    }

    public User updateUser(User user) {
        // Validation
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }

        return userRepository.save(user);
    }

    // Add this new method
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    private UserProfileDTO toProfileDTO(User user) {
        return new UserProfileDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getBio(),
                user.getUniversity(),
                user.getStudyProgram(),
                user.getSkills()
        );
    }
}
