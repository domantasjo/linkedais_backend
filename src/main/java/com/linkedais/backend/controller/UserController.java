package com.linkedais.backend.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
