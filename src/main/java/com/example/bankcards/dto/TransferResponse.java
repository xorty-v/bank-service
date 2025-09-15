package com.example.bankcards.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * TransferResponse is a DTO used to return details of a completed transfer between two cards.
 */
@Data
@AllArgsConstructor
public class TransferResponse {

    /**
     * UUID of the transfer.
     */
    private UUID id;

    /**
     * UUID of the card from which the amount was transferred.
     */
    private UUID fromCardId;

    /**
     * UUID of the card to which the amount was transferred.
     */
    private UUID toCardId;

    /**
     * Amount transferred.
     */
    private BigDecimal amount;
}
