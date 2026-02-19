package com.linkedais.backend.controller;

import com.linkedais.backend.dto.AuthRequest;
import com.linkedais.backend.dto.AuthResponse;
import com.linkedais.backend.model.User;
import com.linkedais.backend.repository.UserRepository;
import com.linkedais.backend.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Authentication Controller
 * 
 * This handles user registration and login.
 * Endpoints in this controller are PUBLIC (no authentication required).
 * 
 * Available endpoints:
 * POST /api/auth/register - Create a new user account
 * POST /api/auth/login    - Login with email and password
 */
@RestController  // This is a REST API controller (returns JSON, not HTML pages)
@RequestMapping("/api/auth")  // All endpoints start with /api/auth
public class AuthController {
    
    // Dependencies we need (injected by Spring)
    private final UserRepository userRepository;     // To save/find users in database
    private final PasswordEncoder passwordEncoder;   // To hash passwords securely
    private final JwtUtil jwtUtil;                  // To generate JWT tokens
    private final AuthenticationManager authenticationManager;  // To verify login credentials

    // Constructor injection - Spring provides all these automatically
    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, 
                         JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    /**
     * REGISTER ENDPOINT
     * POST /api/auth/register
     * 
     * Creates a new user account and returns a JWT token
     * 
     * @param request - Contains email, password, and name from request body
     * @return JWT token and user info if successful, error message if email already exists
     */
    @PostMapping("/register")  // Responds to POST requests at /api/auth/register
    public ResponseEntity<?> register(@Valid @RequestBody AuthRequest request) {
        
        // Step 1: Check if email is already registered
        if (userRepository.existsByEmail(request.getEmail())) {
            // Return 409 Conflict status with error message
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Email already registered"));
        }

        // Step 2: Create new user object
        User user = new User(
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),  // NEVER store plain text password!
                request.getName()
        );
        
        // Step 3: Save user to database
        userRepository.save(user);

        // Step 4: Create JWT token claims (data stored in the token)
        Map<String, Object> claims = new HashMap<>();
        claims.put("name", user.getName());
        claims.put("email", user.getEmail());
        
        // Step 5: Generate JWT token
        String token = jwtUtil.generateToken(user.getEmail(), claims);
        
        // Step 6: Return success response with token
        return ResponseEntity.ok(new AuthResponse(token, user.getEmail(), user.getName()));
    }

    /**
     * LOGIN ENDPOINT
     * POST /api/auth/login
     * 
     * Verifies user credentials and returns a JWT token
     * 
     * @param request - Contains email and password from request body
     * @return JWT token and user info if credentials are valid, error if invalid
     */
    @PostMapping("/login")  // Responds to POST requests at /api/auth/login
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest request) {
        try {
            // Step 1: Verify email and password using Spring Security's AuthenticationManager
            // This checks if password matches the hashed password in database
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            // Step 2: If we get here, credentials are correct. Fetch user from database
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Step 3: Create JWT token claims
            Map<String, Object> claims = new HashMap<>();
            claims.put("name", user.getName());
            claims.put("email", user.getEmail());

            // Step 4: Generate JWT token
            String token = jwtUtil.generateToken(user.getEmail(), claims);
            
            // Step 5: Return success response with token
            return ResponseEntity.ok(new AuthResponse(token, user.getEmail(), user.getName()));
            
        } catch (Exception e) {
            // Authentication failed (wrong email or password)
            // Return 401 Unauthorized with error message
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid credentials"));
        }
    }
}
