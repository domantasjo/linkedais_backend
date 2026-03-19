package com.linkedais.backend.controller;

import com.linkedais.backend.model.User;
import com.linkedais.backend.service.UserService;
import com.linkedais.backend.dto.UpdateProfileRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.linkedais.backend.dto.UserProfileDTO;
import com.linkedais.backend.dto.UpdateNameRequest;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;

/**
 * User Controller
 * 
 * This handles endpoints for logged-in users.
 * All endpoints here are PROTECTED (require valid JWT token).
 */
@RestController  // This is a REST API controller
@RequestMapping("/api/user")  // All endpoints start with /api/user
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    /**
     * GET CURRENT USER ENDPOINT
     * GET /api/user/me
     * 
     * Returns information about the currently logged-in user.
     * Requires valid JWT token in Authorization header.
     * 
     * @param authentication - Spring Security automatically provides this.
     *                        It contains info about the authenticated user from the JWT token.
     * @return User's email and roles/authorities
     */
    @GetMapping("/me")  // Responds to GET requests at /api/user/me
    public Map<String, Object> getCurrentUser(Authentication authentication) {
        
        // Create a response map
        Map<String, Object> response = new HashMap<>();
        
        // authentication.getName() returns the "subject" from JWT (user's email)
        response.put("email", authentication.getName());
        
        // authentication.getAuthorities() returns the user's roles (e.g., "ROLE_USER")
        response.put("authorities", authentication.getAuthorities());
        
        return response;
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getPublicProfile(@PathVariable Long id) {
        UserProfileDTO profile = userService.getPublicProfile(id);
        return ResponseEntity.ok(profile);
    }

    /**
     * GET CURRENT USER'S PROFILE
     * GET /api/user/profile
     */
    @GetMapping("/profile")
    public ResponseEntity<UserProfileDTO> getMyProfile(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(userService.getMyProfile(email));
    }

    /**
     * UPDATE CURRENT USER'S PROFILE
     * PUT /api/user/profile
     */
    @PutMapping("/profile")
    public ResponseEntity<UserProfileDTO> updateProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateProfileRequest request) {
        String email = authentication.getName();
        return ResponseEntity.ok(userService.updateProfile(email, request));
    }

}
