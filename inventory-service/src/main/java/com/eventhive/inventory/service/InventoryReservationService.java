package com.eventhive.inventory.service;

import com.eventhive.inventory.entity.InventoryReservationEntity;
import com.eventhive.inventory.repository.InventoryReservationRepository;
import com.eventhive.shared.events.InventoryReservedEvent;
import com.eventhive.shared.events.OrderCreatedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class InventoryReservationService {

    private final InventoryReservationRepository reservationRepository;
    private final InventoryReservedPublisher reservedPublisher;

    public InventoryReservationService(InventoryReservationRepository reservationRepository,
                                       InventoryReservedPublisher reservedPublisher) {
        this.reservationRepository = reservationRepository;
        this.reservedPublisher = reservedPublisher;
    }

    @Transactional
    public void reserveForOrder(OrderCreatedEvent event) {
        String idempotencyKey = "inv-" + event.idempotencyKey();
        reservationRepository.findByIdempotencyKey(idempotencyKey)
            .ifPresent(ignored -> { return; });

        UUID reservationId = UUID.randomUUID();
        var reservation = new InventoryReservationEntity();
        reservation.setId(reservationId);
        reservation.setOrderId(event.orderId());
        reservation.setStatus("RESERVED");
        reservation.setIdempotencyKey(idempotencyKey);
        reservation.setCreatedAt(Instant.now());
        reservationRepository.save(reservation);

        InventoryReservedEvent reserved = new InventoryReservedEvent(
            reservationId, event.orderId(), "RESERVED", Instant.now());
        reservedPublisher.publish(reserved);
    }
}
