package com.example.bankcards.service;

import com.example.bankcards.dto.CardRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.CardRepo;
import com.example.bankcards.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Service class for managing bank cards.
 * Provides functionality to create, retrieve, update, delete, block, and activate cards.
 */
@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepo cardRepo;
    private final UserRepo userRepo;

    /**
     * Creates a new card for a user.
     *
     * @param request the CardRequest containing card details
     * @return the created Card entity
     * @throws RuntimeException if the user is not found
     */
    public Card createCard(CardRequest request) {
        User user = userRepo.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Card card = Card.builder()
                .number(request.getNumber())
                .user(user)
                .expiry(request.getExpiry())
                .balance(request.getBalance())
                .status(CardStatus.ACTIVE)
                .build();

        return cardRepo.save(card);
    }

    /**
     * Retrieves a card by its ID.
     *
     * @param id the UUID of the card
     * @return the Card entity
     * @throws RuntimeException if the card is not found
     */
    public Card getCard(UUID id) {
        Card card = cardRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Card not found"));

        if (card.getStatus() == CardStatus.DELETED) {
            throw new RuntimeException("Card is deleted");
        }

        return card;
    }

    /**
     * Retrieves paginated cards belonging to a specific user.
     *
     * @param username the username of the card owner
     * @param page     the page number
     * @param size     the page size
     * @return a Page of Card entities excluding deleted cards
     * @throws RuntimeException if the user is not found
     */
    public Page<Card> getCardsByUser(String username, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("expiry").descending());

        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return cardRepo.findAllByUserAndStatusNot(user, CardStatus.DELETED, pageable);
    }

    /**
     * Retrieves paginated cards by their status.
     *
     * @param status the CardStatus to filter
     * @param page   the page number
     * @param size   the page size
     * @return a Page of Card entities
     */
    public Page<Card> getCardsByStatus(CardStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("expiry").descending());
        return cardRepo.findAllByStatus(status, pageable);
    }

    /**
     * Marks a card as deleted.
     *
     * @param id the UUID of the card
     */
    public void deleteCard(UUID id) {
        Card card = getCard(id);
        card.setStatus(CardStatus.DELETED);
        cardRepo.save(card);
    }

    /**
     * Blocks a card.
     *
     * @param id the UUID of the card
     */
    public void blockCard(UUID id) {
        Card card = getCard(id);
        card.setStatus(CardStatus.BLOCKED);
        cardRepo.save(card);
    }

    /**
     * Activates a card.
     *
     * @param id the UUID of the card
     */
    public void activateCard(UUID id) {
        Card card = getCard(id);
        card.setStatus(CardStatus.ACTIVE);
        cardRepo.save(card);
    }

    /**
     * Updates the card status to EXPIRED if the expiry date has passed.
     *
     * @param card the Card to check
     * @return the updated Card entity
     */
    public Card updateCardStatusIfExpired(Card card) {
        if (card.getStatus() != CardStatus.EXPIRED && card.getExpiry().isBefore(LocalDate.now())) {
            card.setStatus(CardStatus.EXPIRED);
            cardRepo.save(card);
        }
        return card;
    }
}
