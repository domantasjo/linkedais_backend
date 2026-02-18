package com.linkedais.backend.security;

import com.linkedais.backend.service.CustomUserDetailsService;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

/**
 * Security Configuration - The brain of the authentication system
 * 
 * This class configures:
 * 1. Which URLs are public vs protected (require login)
 * 2. How to handle JWT tokens
 * 3. CORS settings (allows frontend on different domain to access API)
 * 4. Password encryption
 * 5. Authentication manager
 */
@Configuration  // This is a configuration class
public class SecurityConfig {
  
  // Dependencies injected by Spring
  private final JwtUtil jwtUtil;
  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final CustomUserDetailsService userDetailsService;

  // Constructor injection - Spring provides these automatically
  public SecurityConfig(JwtUtil jwtUtil, JwtAuthenticationFilter jwtAuthenticationFilter,
                       CustomUserDetailsService userDetailsService) {
    this.jwtUtil = jwtUtil;
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    this.userDetailsService = userDetailsService;
  }

  /**
   * Main security configuration
   * Defines which endpoints need authentication and how to handle requests
   */
  @Bean  // This method creates a bean that Spring manages
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      // Disable CSRF (Cross-Site Request Forgery) protection
      // We don't need it because we use JWT tokens instead of cookies
      .csrf(csrf -> csrf.disable())
      
      // Enable CORS (Cross-Origin Resource Sharing)
      // Allows frontend (like React app on localhost:3000) to call our API
      .cors(cors -> cors.configurationSource(corsConfigSource()))
      
      // Use STATELESS sessions
      // This means server doesn't store session data - everything is in the JWT token
      .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      
      // Configure which URLs need authentication
      .authorizeHttpRequests(auth -> auth
        // These URLs are PUBLIC - anyone can access without login
        .requestMatchers("/api/auth/**", "/h2-console/**").permitAll()
        
        // All other URLs require authentication (must have valid JWT token)
        .anyRequest().authenticated())
      
      // What to do when user is not authenticated
      .exceptionHandling(ex -> ex
        .authenticationEntryPoint((req, res, e) -> res.sendError(401, "Unauthorized")))
      
      // Allow H2 console to be displayed in iframe (for development only)
      .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

    // Add our JWT filter BEFORE Spring's default authentication filter
    // This ensures JWT tokens are checked first
    http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    
    return http.build();
  }

  /**
   * CORS Configuration
   * Specifies which external websites can call our API
   */
  @Bean
  public CorsConfigurationSource corsConfigSource() {
    CorsConfiguration config = new CorsConfiguration();
    
    // Allow requests from frontend running on localhost:3000
    config.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
    
    // Allow these HTTP methods
    config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    
    // Allow all headers
    config.setAllowedHeaders(Arrays.asList("*"));
    
    // Allow credentials (cookies, authorization headers)
    config.setAllowCredentials(true);
    
    // Apply CORS settings to all endpoints
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }

  /**
   * Password Encoder Bean
   * 
   * BCrypt is a secure way to hash passwords
   * - Never stores passwords in plain text
   * - Uses salt (random data) to prevent rainbow table attacks
   * - Slow by design to prevent brute force attacks
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * Authentication Manager
   * 
   * This is used to verify user credentials (email + password)
   * when they try to login
   */
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }
}
