package com.example.bankcards.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Entity representing a bank card.
 * Stores information about card number, owner, expiry, status, balance, and masked number.
 */
@Entity
@Table(name = "cards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Card {

    /**
     * Unique identifier of the card.
     */
    @Id
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id;

    /**
     * Full card number.
     */
    @Column(name = "number", nullable = false, length = 19)
    private String number;

    /**
     * Owner of the card.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Expiry date of the card.
     */
    @Column(name = "expiry", nullable = false)
    private LocalDate expiry;

    /**
     * Status of the card(ACTIVE, BLOCKED).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CardStatus status;

    /**
     * Current balance of the card.
     */
    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    /**
     * The last 4 digits of the card number.
     */
    @Column(name = "last4", nullable = false)
    private String last4;

    /**
     * Version field for optimistic locking.
     */
    @Version
    private Long version;

    /**
     * Method to initialize default values before saving to the database.
     */
    @PrePersist
    public void prePersist() {
        if (id == null) id = UUID.randomUUID();
        if (balance == null) balance = BigDecimal.valueOf(0.0);
        if (status == null) status = CardStatus.ACTIVE;
        if (last4 == null && number != null && number.length() >= 4) {
            last4 = number.substring(number.length() - 4);
        }
    }

    /**
     * Returns the masked card number.
     */
    public String getMaskedNumber() {
        if (id == null || number.length() < 4) return "****";
        return "**** **** **** " + number.substring(number.length() - 4);
    }
}
