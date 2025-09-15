package com.example.bankcards.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Represents a user of the banking system.
 * Stores user credentials, account status, roles, and creation timestamp.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    /** Unique identifier for the user. */
    @Id
    @EqualsAndHashCode.Include
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id;

    /** Username used for authentication. Must be unique. */
    @Column(name = "username", nullable = false, unique = true, length = 64)
    private String username;

    /** Encrypted password for authentication. */
    @Column(name = "password", nullable = false, length = 255)
    private String password;

    /** Indicates if the user account is enabled. */
    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;

    /** Roles assigned to the user. */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    /** Timestamp when the account was created. */
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    /**
     * Automatically sets the user ID and creation timestamp before persisting.
     */
    @PrePersist
    public void prePersist() {
        if (this.id == null) this.id = UUID.randomUUID();
        if (this.createdAt == null) this.createdAt = Instant.now();
    }

    /**
     * Getter for the enabled status.
     * @return true if the account is enabled, false otherwise
     */
    public Boolean getEnabled() {
        return enabled;
    }
}
