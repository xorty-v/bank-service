package com.example.bankcards.dto;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * CardResponse is a DTO used to return card details to the client.
 */
@Data
@AllArgsConstructor
public class CardResponse {

    /**
     * Unique identifier of the card.
     */
    private UUID id;

    /**
     * Masked card number(only last 4 digits visible).
     */
    private String maskedNumber;

    /**
     * Username of the card owner.
     */
    private String ownerUsername;

    /**
     * Expiry date of the card.
     */
    private LocalDate expiry;

    /**
     * Current status of the card(ACTIVE, BLOCKED).
     */
    private CardStatus status;

    /**
     * Current balance on the card.
     */
    private BigDecimal balance;

    /**
     * Factory method that converts a Card entity into a CardResponse DTO.
     *
     * @param card
     * @return a response DTO representing the card
     */
    public static CardResponse fromEntity(Card card) {
        return new CardResponse(
                card.getId(),
                card.getMaskedNumber(),
                card.getUser().getUsername(),
                card.getExpiry(),
                card.getStatus(),
                card.getBalance()
        );
    }
}
