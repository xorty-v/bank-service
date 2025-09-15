package com.example.bankcards.dto;

import lombok.Data;

/**
 * RegisterRequest is a DTO used to receive user details when creating a new account.
 */
@Data
public class RegisterRequest {

    /**
     * Username for the new user account.
     */
    private String username;

    /**
     * The password for the new user account.
     */
    private String password;
}
