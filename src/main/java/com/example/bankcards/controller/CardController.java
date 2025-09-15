package com.example.bankcards.controller;

import com.example.bankcards.dto.CardRequest;
import com.example.bankcards.dto.CardResponse;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * CardController provides REST endpoints for managing bank cards.
 * Implements operations for users and admins.
 */
@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
@Tag(name = "Cards", description = "Endpoints for managing bank cards")
public class CardController {

    private final CardService cardService;

    /**
     * Retrieves the authenticated user's cards in a paginated format.
     *
     * @param page
     * @param size
     * @return a page of CardResponse objects belonging to the user
     */
    @Operation(
            summary = "See my cards",
            description = "Returns a paginated list of cards belonging to the authenticated user"
    )
    @GetMapping("/my")
    public Page<CardResponse> getMyCards(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return cardService.getCardsByUser(username, page, size)
                .map(CardResponse::fromEntity);
    }

    /**
     * Retrieves details of a specific card by its ID.
     * If the card's expiry date has passed, its status will be updated to EXPIRED
     *
     * @param id
     * @return CardResponse
     */
    @Operation(
            summary = "Get a card",
            description = "Retrieve details of a specific card by its ID"
    )
    @GetMapping("/{id}")
    public CardResponse getCard(@PathVariable UUID id) {
        Card card = cardService.getCard(id);
        card = cardService.updateCardStatusIfExpired(card);
        return CardResponse.fromEntity(card);
    }

    /**
     * Retrieves cards filtered by their status.
     *
     * @param status
     * @param page
     * @param size
     * @return a page of CardResponse objects with the specified status
     */
    @Operation(
            summary = "Find cards by status",
            description = "Returns a paginated list of cards filtered by their status (ADMIN only)"
    )
    @GetMapping("/status/{status}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<CardResponse> getCardsByStatus(@PathVariable("status") CardStatus status,
                                               @RequestParam(name = "page", defaultValue = "0") int page,
                                               @RequestParam(name = "size", defaultValue = "10") int size) {
        return cardService.getCardsByStatus(status, page, size)
                .map(CardResponse::fromEntity);
    }

    /**
     * Creates a new card for a user.
     *
     * @param request CardRequest
     * @return CardResponse
     */
    @Operation(
            summary = "Create a card",
            description = "ADMIN creates a new card for a user"
    )
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public CardResponse createCard(@RequestBody @Valid CardRequest request) {
        return CardResponse.fromEntity(cardService.createCard(request));
    }

    /**
     * Blocks a card by ID.
     *
     * @param id
     * @return CardResponse with updated card status
     */
    @Operation(summary = "Block a card", description = "ADMIN blocks a specific card")
    @PostMapping("/{id}/block")
    @PreAuthorize("hasAuthority('ADMIN')")
    public CardResponse blockCard(@PathVariable UUID id) {
        cardService.blockCard(id);
        return CardResponse.fromEntity(cardService.getCard(id));
    }

    /**
     * Activates a card by ID.
     *
     * @param id
     * @return CardResponse with updated card status
     */
    @Operation(summary = "Activate a card", description = "ADMIN activates a specific card")
    @PostMapping("/{id}/activate")
    @PreAuthorize("hasAuthority('ADMIN')")
    public CardResponse activateCard(@PathVariable UUID id) {
        cardService.activateCard(id);
        return CardResponse.fromEntity(cardService.getCard(id));
    }

    /**
     * Allows a user to request blocking of their card.
     *
     * @param id
     * @return CardResponse with updated card status
     * @throws RuntimeException if the card does not belong to the authenticated user
     */
    @Operation(
            summary = "Request block of a card",
            description = "USER requests an ADMIN to block their own card"
    )
    @PostMapping("/{id}/request-block")
    @PreAuthorize("hasAuthority('USER')")
    public CardResponse requestBlockCard(@PathVariable UUID id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Card card = cardService.getCard(id);
        if (!card.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You can only request block for your own cards");
        }
        cardService.blockCard(id);
        return CardResponse.fromEntity(cardService.getCard(id));
    }

    /**
     * Deletes a card by ID.
     *
     * @param id
     */
    @Operation(summary = "Delete a card", description = "ADMIN deletes a card")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteCard(@PathVariable("id") UUID id) {
        cardService.deleteCard(id);
    }
}
