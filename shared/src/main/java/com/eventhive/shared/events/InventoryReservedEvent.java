package com.eventhive.shared.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.UUID;

public record InventoryReservedEvent(
    @JsonProperty("reservationId") UUID reservationId,
    @JsonProperty("orderId") UUID orderId,
    @JsonProperty("status") String status,
    @JsonProperty("timestamp") Instant timestamp
) {
    @JsonCreator
    public InventoryReservedEvent {}
}
