package com.example.bankcards.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a money transfer between two cards.
 * Each transfer has the from card, to card, amount, and timestamp.
 */
@Entity
@Table(name = "transfers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transfer {

    /** Unique identifier for the transfer. */
    @Id
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id;

    /** The from card from which the amount is transferred. */
    @ManyToOne
    @JoinColumn(name = "from_card_id", nullable = false)
    private Card fromCard;

    /** The to card to which the amount is transferred. */
    @ManyToOne
    @JoinColumn(name = "to_card_id", nullable = false)
    private Card toCard;

    /** Amount of money transferred. */
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    /** Timestamp when the transfer was created. */
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    /**
     * Automatically sets the ID and creation timestamp.
     */
    @PrePersist
    public void prePersist() {
        if (id == null) id = UUID.randomUUID();
        if (createdAt == null) createdAt = Instant.now();
    }
}
