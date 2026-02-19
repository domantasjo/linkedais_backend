package com.linkedais.backend.service;

import com.linkedais.backend.model.User;
import com.linkedais.backend.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Custom User Details Service
 * 
 * This service loads user information from our database for Spring Security.
 * Spring Security uses this during login to verify credentials.
 * 
 * When user tries to login:
 * 1. Spring calls loadUserByUsername(email)
 * 2. We fetch user from database
 * 3. Spring compares passwords
 * 4. If match, user is authenticated
 */
@Service  // This is a Spring service component
public class CustomUserDetailsService implements UserDetailsService {
    
    private final UserRepository userRepository;  // To fetch users from database

    // Constructor injection - Spring provides UserRepository
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Load user by email (username)
     * 
     * Spring Security calls this method during login.
     * 
     * @param email - The email address user entered when logging in
     * @return UserDetails object that Spring Security uses to verify password
     * @throws UsernameNotFoundException if email doesn't exist in database
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        
        // Step 1: Try to find user in database by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Step 2: Convert our User entity to Spring Security's UserDetails format
        // UserDetails is what Spring Security needs to verify login
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),     // Username (we use email as username)
                user.getPassword(),  // Hashed password from database
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))  // User's roles/permissions
        );
    }
}
