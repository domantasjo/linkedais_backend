package com.linkedais.backend.controller;

import java.io.IOException;
import java.nio.file.attribute.UserPrincipal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkedais.backend.model.User;
import com.linkedais.backend.repository.UserRepository;
import com.linkedais.backend.service.UserStatusService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import com.linkedais.backend.dto.EnrollmentResponse;
import com.linkedais.backend.dto.UpdateProfileRequest;
import com.linkedais.backend.dto.UserProfileDTO;
import com.linkedais.backend.service.EnrollmentService;
import com.linkedais.backend.service.ImageUploadService;
import com.linkedais.backend.service.UserService;

import jakarta.validation.Valid;

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
    private final EnrollmentService enrollmentService;
    private final ImageUploadService imageUploadService;
    private final UserStatusService userStatusService;
    private final UserRepository userRepository;

    public UserController(UserService userService, EnrollmentService enrollmentService, ImageUploadService imageUploadService, UserStatusService userStatusService, UserRepository userRepository) {
        this.userService = userService;
        this.enrollmentService = enrollmentService;
        this.imageUploadService = imageUploadService;
        this.userStatusService = userStatusService;
        this.userRepository = userRepository;
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

    /**
     * GET CURRENT STUDENT'S GRADES
     * GET /api/user/grades
     */
    @GetMapping("/grades")
    public ResponseEntity<List<EnrollmentResponse>> getMyGrades(Authentication authentication) {
        return ResponseEntity.ok(enrollmentService.getStudentEnrollmentsByEmail(authentication.getName()));
    }

    /**
     * UPLOAD PROFILE PICTURE
     * POST /api/user/profile-picture
     */
    @PostMapping("/profile-picture")
    public ResponseEntity<?> uploadProfilePicture(
            Authentication authentication,
            @RequestParam("image") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "File is empty"));
            }
            String base64 = imageUploadService.convertToBase64(file);
            userService.updateProfilePicture(authentication.getName(), base64);
            // Return only the new base64 (not the whole user) to keep the response small
            // and to avoid any @Lob re-read quirks after save().
            return ResponseEntity.ok(Map.of("profilePictureBase64", base64));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to process image"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Handles uploads that exceed spring.servlet.multipart.max-file-size.
     * Returns clean JSON instead of Spring's default HTML error page so the frontend can show a meaningful message.
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Map<String, String>> handleMaxUploadSize(MaxUploadSizeExceededException e) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(Map.of("error", "Failas per didelis. Maksimalus dydis: 10MB"));
    }

    @PostMapping("/heartbeat")
    public ResponseEntity<Void> handleHeartbeat(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        userStatusService.processHeartbeat(user.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/statuses")
    public ResponseEntity<Map<Long, String>> getStatuses(@RequestParam List<Long> ids) {
        Map<Long, String> statuses = new HashMap<>();
        for (Long id : ids) {
            statuses.put(id, userStatusService.getStatus(id));
        }
        return ResponseEntity.ok(statuses);
    }
}
