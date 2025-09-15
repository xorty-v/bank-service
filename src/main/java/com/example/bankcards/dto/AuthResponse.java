package com.example.bankcards.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * AuthResponse is a DTO used to return a JWT authentication token after a successful login.
 */
@Data
@AllArgsConstructor
public class AuthResponse {

    private String token;
}
