package com.linkedais.backend.service;

import com.linkedais.backend.dto.ChangePasswordRequest;
import com.linkedais.backend.model.User;
import com.linkedais.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void changePassword(String email, ChangePasswordRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Vartotojas nerastas"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Neteisingas dabartinis slaptažodis");
        }

        if (request.getNewPassword().length() < 6) {
            throw new RuntimeException("Naujas slaptažodis turi būti bent 6 simbolių");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}