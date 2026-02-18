package com.linkedais.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Authentication Request DTO (Data Transfer Object)
 * 
 * This class represents the data sent by the client when registering or logging in.
 * DTO = a simple object used to transfer data between client and server (no business logic)
 * 
 * Used for:
 * - POST /api/auth/register (needs email, password, name)
 * - POST /api/auth/login (needs email, password)
 */
public class AuthRequest {
    
    // Email field - must not be blank and must be valid email format
    @NotBlank  // Validation: Cannot be null or empty string
    @Email     // Validation: Must be valid email format (e.g., user@example.com)
    private String email;

    // Password field - must not be blank
    @NotBlank  // Validation: Cannot be null or empty string
    private String password;

    // Name field - optional (only used for registration)
    private String name;

    // ==================== GETTERS AND SETTERS ====================
    // Spring uses these to convert JSON to Java object and vice versa
    
    /**
     * Get the email
     * @return User's email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the email
     * @param email - Email from request body
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get the password
     * @return User's password (plain text from request)
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the password
     * @param password - Password from request body
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Get the name
     * @return User's display name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name
     * @param name - Name from request body (used in registration)
     */
    public void setName(String name) {
        this.name = name;
    }
}
