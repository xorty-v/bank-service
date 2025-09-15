package com.example.bankcards.controller;

import com.example.bankcards.dto.AuthResponse;
import com.example.bankcards.dto.LoginRequest;
import com.example.bankcards.dto.RegisterRequest;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.RoleRepo;
import com.example.bankcards.repository.UserRepo;
import com.example.bankcards.security.JwtProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AuthController provides REST endpoints for user authentication and registration.
 * Supports login with JWT token generation and user registration with role assignment.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user login and registration")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;

    /**
     * Authenticates a user and generates a JWT token.
     *
     * @param request LoginRequest containing username and password
     * @return AuthResponse containing a JWT token if authentication succeeds
     */
    @Operation(
            summary = "Authenticate user and generate JWT",
            description = "Accepts username and password, verifies credentials, and returns a JWT token for authorized access."
    )
    @PostMapping("login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        String token = jwtProvider.generateToken(authentication.getName());
        return new AuthResponse(token);
    }

    /**
     * Registers a new user account.
     *
     * @param request RegisterRequest containing username and password
     * @return success or error message as String
     */
    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account with a username, password, and role. Returns user details without password."
    )
    @PostMapping("register")
    public String register(@RequestBody RegisterRequest request) {
        if (userRepo.findByUsername(request.getUsername()).isPresent()) {
            return "Username is already in use";
        }

        var user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(true)
                .build();

        Role userRole = roleRepo.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Role USER not found"));
        user.getRoles().add(userRole);

        userRepo.save(user);
        return "User registered successfully";
    }
}
