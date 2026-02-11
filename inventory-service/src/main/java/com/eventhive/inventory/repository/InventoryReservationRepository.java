package com.eventhive.inventory.repository;

import com.eventhive.inventory.entity.InventoryReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface InventoryReservationRepository extends JpaRepository<InventoryReservationEntity, UUID> {

    Optional<InventoryReservationEntity> findByIdempotencyKey(String idempotencyKey);
}
