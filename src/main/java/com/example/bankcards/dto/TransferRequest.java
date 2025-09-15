package com.example.bankcards.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * TransferRequest is a DTO used to request a transfer of funds between two cards.
 */
@Data
public class TransferRequest {

    /**
     * UUID of the card to transfer money from.
     */
    @NotNull(message = "From card ID is required")
    private UUID fromCardId;

    /**
     * UUID of the card to transfer money to.
     */
    @NotNull(message = "To card ID is required")
    private UUID toCardId;

    /**
     * Amount to transfer. Must be positive.
     */
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;
}
