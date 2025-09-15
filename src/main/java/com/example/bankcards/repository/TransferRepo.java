package com.example.bankcards.repository;

import com.example.bankcards.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Repository interface for managing Transfer entities.
 * Provides standard CRUD operations for transfers between cards.
 */
public interface TransferRepo extends JpaRepository<Transfer, UUID> {
}
