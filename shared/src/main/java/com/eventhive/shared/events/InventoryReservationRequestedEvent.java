package com.eventhive.shared.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record InventoryReservationRequestedEvent(
    @JsonProperty("reservationId") UUID reservationId,
    @JsonProperty("orderId") UUID orderId,
    @JsonProperty("items") List<OrderItemDto> items,
    @JsonProperty("timestamp") Instant timestamp,
    @JsonProperty("idempotencyKey") String idempotencyKey
) {
    @JsonCreator
    public InventoryReservationRequestedEvent {}
}
