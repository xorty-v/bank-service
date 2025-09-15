package com.example.bankcards.config;

import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.RoleRepo;
import com.example.bankcards.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * DataInitializer is a Spring component that initializes default roles and an admin user
 * in the system at application startup.
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;


    /**
     * Runs the data initialization process at application startup.
     * Ensures that roles "ADMIN" and "USER" exist, and creates an admin user
     * with username "admin" and password "admin" if it doesn't already exist.
     *
     * @param args
     */
    @Override
    public void run(String... args) {
        Role adminRole = roleRepo.findByName("ADMIN")
                .orElseGet(() -> roleRepo.save(new Role("ADMIN")));

        Role userRole = roleRepo.findByName("USER")
                .orElseGet(() -> roleRepo.save(new Role("USER")));

        if (userRepo.findByUsername("admin").isEmpty()) {
            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin"))
                    .enabled(true)
                    .roles(Set.of(adminRole, userRole))
                    .build();
            userRepo.save(admin);
        }
    }
}
