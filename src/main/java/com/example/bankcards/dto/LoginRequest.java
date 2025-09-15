package com.example.bankcards.dto;

import lombok.Data;

/**
 * LoginRequest is a DTO used to receive user credentials during the authentication process.
 */
@Data
public class LoginRequest {

    /**
     * The username of the user trying to log in.
     */
    private String username;

    /**
     * The raw password of the user.
     */
    private String password;
}
