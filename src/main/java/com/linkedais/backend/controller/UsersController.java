package com.linkedais.backend.controller;

import com.linkedais.backend.dto.UserProfileDTO;
import com.linkedais.backend.dto.UserSearchResponse;
import com.linkedais.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    /**
     * SEARCH USERS BY NAME
     * GET /api/users/search?name={query}
     */
    @GetMapping("/search")
    public ResponseEntity<List<UserSearchResponse>> searchUsers(
            @RequestParam String name,
            Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(userService.searchUsers(name, email));
    }

    /**
     * GET PUBLIC USER PROFILE BY ID
     * GET /api/users/{userId}
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileDTO> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getPublicProfile(userId));
    }
}
