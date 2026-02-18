package com.linkedais.backend.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Collections;

/* 
  JWT Authentication Filter
  
  This filter runs on EVERY incoming HTTP request.
  Its job is to check if the request has a valid JWT token and authenticate the user.
  
  Think of it like a security guard checking everyone's ID badge before letting them in.
  
  Flow:
  1. Check if request has "Authorization: Bearer <token>" header
  2. If yes, validate the token
  3. If token is valid, mark the user as authenticated
  4. Let the request continue to the actual endpoint
 */
@Component  // Spring manages this as a bean
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  
  private final JwtUtil jwtUtil;  // Helper to validate tokens
  
  // Constructor injection - Spring automatically provides JwtUtil
  public JwtAuthenticationFilter(JwtUtil jwtUtil) { 
    this.jwtUtil = jwtUtil; 
  }

  /**
    This method runs once for every HTTP request
    
    @param req - The incoming HTTP request
    @param res - The HTTP response we'll send back
    @param chain - Used to continue processing the request
   */
  @Override
  protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) 
      throws ServletException, IOException {
    
    // Step 1: Get the "Authorization" header from the request
    // Example: "Authorization: Bearer eyJhbGci..."
    String header = req.getHeader("Authorization");
    
    // Step 2: Check if header exists and starts with "Bearer "
    if (header != null && header.startsWith("Bearer ")) {
      
      // Step 3: Extract the actual token (remove "Bearer " prefix)
      // "Bearer eyJhbGci..." becomes "eyJhbGci..."
      String token = header.substring(7);
      
      try {
        // Step 4: Validate the token and extract information from it
        var claims = jwtUtil.validate(token).getPayload();  // This throws exception if invalid
        
        // Step 5: Get the user's email (subject) from the token
        String subject = claims.getSubject();
        
        // Step 6: Create an authentication object for Spring Security
        // This tells Spring "this user is logged in with ROLE_USER"
        var auth = new UsernamePasswordAuthenticationToken(
            subject,  // User email
            null,     // No password needed (already authenticated via token)
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))  // User's roles/permissions
        );
        
        // Step 7: Store authentication in Spring Security's context
        // Now the entire application knows this user is authenticated
        SecurityContextHolder.getContext().setAuthentication(auth);
        
      } catch (Exception e) {
        // If token validation fails (expired, invalid signature, etc.)
        // We just ignore it and let the request continue as UNAUTHENTICATED
        // Protected endpoints will reject unauthenticated requests
      }
    }
    
    // Step 8: Continue processing the request (move to next filter or endpoint)
    chain.doFilter(req, res);
  }
}
