package com.linkedais.backend.repository;

import com.linkedais.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * User Repository
 * 
 * This interface provides methods to interact with the "users" database table.
 * Spring Data JPA automatically implements all these methods - we just declare them!
 * 
 * JpaRepository gives us built-in methods like:
 * - save() - Insert or update a user
 * - findById() - Find user by ID
 * - findAll() - Get all users
 * - delete() - Remove a user
 * 
 * Plus our custom methods below.
 */
@Repository  // This is a data access component
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Find a user by their email address
     * 
     * Spring Data JPA automatically generates the SQL query for this!
     * SELECT * FROM users WHERE email = ?
     * 
     * @param email - The email to search for
     * @return Optional containing User if found, empty Optional if not found
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Check if a user with given email exists
     * 
     * Generates SQL: SELECT COUNT(*) > 0 FROM users WHERE email = ?
     * 
     * @param email - The email to check
     * @return true if email exists, false otherwise
     */
    boolean existsByEmail(String email);
}
