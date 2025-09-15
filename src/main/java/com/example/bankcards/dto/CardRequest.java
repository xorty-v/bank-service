package com.example.bankcards.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * CardRequest is a DTO used for creating or updating user's card.
 */
@Data
public class CardRequest {

    /**
     * The card number.
     * Must be 16 digits long.
     */
    @NotBlank(message = "Card number is required")
    @Pattern(regexp = "\\d{16}", message = "Card number must be 16 digits")
    private String number;

    /**
     * The unique identifier of the user who owns the card.
     */
    @NotNull(message = "User ID is required")
    private UUID userId;

    /**
     * The expiry date of the card.
     * Must be a date in the future.
     */
    @NotNull(message = "Expiry date is required")
    @Future(message = "Expiry date must be in the future")
    private LocalDate expiry;

    /**
     * The balance on the card.
     * Cannot be negative.
     */
    @NotNull(message = "Balance is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Balance cannot be negative")
    private BigDecimal balance;
}
