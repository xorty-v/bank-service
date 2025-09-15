package com.example.bankcards.repository;

import com.example.bankcards.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing User entities.
 * Provides standard CRUD operations and custom queries for users.
 */
public interface UserRepo extends JpaRepository<User, UUID> {

    /**
     * Finds a user by their username, fetching roles eagerly.
     *
     * @param username the username to search for
     * @return an Optional containing the User if found, or empty otherwise
     */
    @EntityGraph(attributePaths = "roles")
    Optional<User> findByUsername(String username);
}
