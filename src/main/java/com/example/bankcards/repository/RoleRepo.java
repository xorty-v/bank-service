package com.example.bankcards.repository;

import com.example.bankcards.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for managing Role entities.
 * Provides CRUD operations and custom queries to find roles by name.
 */
public interface RoleRepo extends JpaRepository<Role, Long> {

    /**
     * Finds a role by its name.
     *
     * @param name the name of the role
     * @return an Optional containing the Role if found, or empty if not found
     */
    Optional<Role> findByName(String Name);
}
