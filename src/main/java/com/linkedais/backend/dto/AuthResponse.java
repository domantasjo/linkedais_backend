package com.linkedais.backend.dto;

/**
 * Authentication Response DTO (Data Transfer Object)
 * 
 * This class represents the data sent back to the client after successful login/registration.
 * DTO = a simple object used to transfer data between server and client
 * 
 * Returned by:
 * - POST /api/auth/register (after successful registration)
 * - POST /api/auth/login (after successful login)
 * 
 * Example JSON response:
 * {
 *   "token": "eyJhbGci...",
 *   "email": "user@example.com",
 *   "name": "John Doe"
 * }
 */
public class AuthResponse {
    
    private String token;  // JWT token - client uses this to authenticate future requests
    private String email;  // User's email address
    private String name;   // User's display name

    /**
     * Constructor
     * 
     * @param token - The JWT token
     * @param email - User's email
     * @param name - User's name
     */
    public AuthResponse(String token, String email, String name) {
        this.token = token;
        this.email = email;
        this.name = name;
    }

    // ==================== GETTERS AND SETTERS ====================
    // Spring uses these to convert Java object to JSON response
    
    /**
     * Get the JWT token
     * @return JWT token string
     */
    public String getToken() {
        return token;
    }

    /**
     * Set the JWT token
     * @param token - JWT token
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Get the user's email
     * @return Email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the user's email
     * @param email - Email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get the user's name
     * @return Display name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the user's name
     * @param name - Display name
     */
    public void setName(String name) {
        this.name = name;
    }
}
