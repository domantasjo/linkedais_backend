package com.linkedais.backend.service;

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
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User no found"));
        return new UserProfileDTO(user.getId(), user.getName());
    }
}
