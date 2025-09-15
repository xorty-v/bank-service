package com.example.bankcards.repository;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing Card entities.
 * Provides CRUD operations and custom queries to find cards by user and status.
 */
public interface CardRepo extends JpaRepository<Card, UUID> {

    /**
     * Finds all cards with a specific status, paginated.
     *
     * @param status the status to filter by
     * @param pageable pagination information
     * @return a page of cards with the given status
     */
    Page<Card> findAllByStatus(CardStatus status, Pageable pageable);

    /**
     * Finds all cards for a user excluding a certain status, paginated.
     *
     * @param user the owner of the cards
     * @param status the status to exclude
     * @param pageable pagination information
     * @return a page of cards for the user excluding the given status
     */
    Page<Card> findAllByUserAndStatusNot(User user, CardStatus status, Pageable pageable);

    /**
     * Finds a card by its ID with a pessimistic write lock.
     *
     * @param id the unique identifier of the card
     * @return an Optional containing the card if found, otherwise empty
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Card c where c.id = :id")
    Optional<Card> findByIdForUpdate(@Param("id") UUID id);

}
