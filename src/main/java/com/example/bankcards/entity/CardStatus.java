package com.example.bankcards.entity;

/**
 * Enum representing the status of a bank card.
 */
public enum CardStatus {

    /** Card is active and can be used for transactions. */
    ACTIVE,

    /** Card is blocked and cannot be used until unblocked by an admin. */
    BLOCKED,

    /** Card has expired. */
    EXPIRED,

    /** Card has been deleted. */
    DELETED
}
