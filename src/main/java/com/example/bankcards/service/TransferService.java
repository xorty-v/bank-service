package com.example.bankcards.service;

import com.example.bankcards.dto.TransferResponse;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.Transfer;
import com.example.bankcards.repository.CardRepo;
import com.example.bankcards.repository.TransferRepo;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Service class for handling money transfers between cards.
 * Provides functionality to perform a transfer from one user's card to another.
 */
@Service
@RequiredArgsConstructor
public class TransferService {

    private final CardRepo cardRepo;
    private final TransferRepo transferRepo;

    /**
     * Transfers money from one card to another.
     *
     * @param fromCardId the UUID of the source card
     * @param toCardId the UUID of the target card
     * @param amount the transfer amount
     * @param username the username of the user initiating the transfer
     * @return a TransferResponse containing details of the completed transfer
     * @throws RuntimeException if any validation fails
     */
    @Transactional(rollbackFor = Exception.class)
    public TransferResponse transfer(UUID fromCardId, UUID toCardId, BigDecimal amount, String username) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be greater than zero");
        }

        Card fromCard = cardRepo.findByIdForUpdate(fromCardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));
        Card toCard = cardRepo.findByIdForUpdate(toCardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));

        if (!fromCard.getStatus().equals(CardStatus.ACTIVE)
                || !toCard.getStatus().equals(CardStatus.ACTIVE)) {
            throw new RuntimeException("Both cards must be active");
        }

        if (!fromCard.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You can only transfer from your own card");
        }

        if (fromCardId.equals(toCardId)) {
            throw new RuntimeException("Cannot transfer to the same card");
        }

        if (fromCard.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds");
        }

        fromCard.setBalance(fromCard.getBalance().subtract(amount));
        toCard.setBalance(toCard.getBalance().add(amount));

        cardRepo.save(fromCard);
        cardRepo.save(toCard);

        Transfer transfer = Transfer.builder()
                .fromCard(fromCard)
                .toCard(toCard)
                .amount(amount)
                .build();

        Transfer savedTransfer = transferRepo.save(transfer);

        return new TransferResponse(
                savedTransfer.getId(),
                fromCard.getId(),
                toCard.getId(),
                savedTransfer.getAmount()
        );
    }
}
