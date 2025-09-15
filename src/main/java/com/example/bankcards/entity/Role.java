package com.example.bankcards.entity;

import jakarta.persistence.*;
import lombok.*;

/** Represents a user role in the system. */
@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    /** Unique identifier of the role, generated automatically. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Name of the role, must be unique. */
    @Column(name = "name", nullable = false, unique = true, length = 32)
    private String name;

    /**
     * Constructor to create a role with a specific name.
     * @param name the name of the role
     */
    public Role(String name) {
        this.name = name;
    }
}
