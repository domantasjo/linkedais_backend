package com.linkedais.backend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

/**
 * JWT (JSON Web Token) Utility Class
 * 
 * This class handles creating and validating JWT tokens for user authentication.
 * Think of JWT as a secure "ticket" that proves a user is logged in.
 * 
 * When a user logs in successfully, we give them a JWT token.
 * They send this token with every request to prove who they are.
 */
@Component  // This tells Spring to create and manage this class automatically
public class JwtUtil {
    
    // Read the secret key from application.properties file
    // This secret is like a password used to sign/verify tokens
    @Value("${jwt.secret}")
    private String secret;
    
    // Read token expiration time from application.properties (in milliseconds)
    // After this time, the token becomes invalid and user must login again
    @Value("${jwt.expiration-ms}")
    private long expirationMs;

    /**
     * Convert our secret string into a cryptographic key
     * This key is used to digitally "sign" the JWT so nobody can fake it
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * Generate a new JWT token for a user
     * 
     * @param subject - Usually the user's email (identifies who the token belongs to)
     * @param claims - Extra information to store in the token (like name, roles, etc.)
     * @return A string token that looks like: "eyJhbGci..." (3 parts separated by dots)
     */
    public String generateToken(String subject, Map<String, Object> claims) {
        Date now = new Date();  // Current time
        Date expiryDate = new Date(now.getTime() + expirationMs);  // When token expires
        
        // Build the JWT token with all the information
        return Jwts.builder()
                .claims(claims)              // Add custom data (name, email, etc.)
                .subject(subject)            // Main identifier (user email)
                .issuedAt(now)              // When the token was created
                .expiration(expiryDate)     // When the token expires
                .signWith(getSigningKey())  // Sign it with our secret key (proves it's authentic)
                .compact();                 // Convert to final string format
    }
    
    /**
     * Validate and parse a JWT token
     * 
     * This checks if:
     * 1. The token was actually created by us (signature is valid)
     * 2. The token hasn't expired
     * 3. The token hasn't been tampered with
     * 
     * @param token - The JWT string to validate
     * @return Parsed token data (claims) if valid
     * @throws Exception if token is invalid, expired, or tampered with
     */
    public Jws<Claims> validate(String token){
        return Jwts.parser()
                .verifyWith(getSigningKey())  // Check signature matches our secret
                .build()
                .parseSignedClaims(token);    // Parse and verify the token
    }
    
}