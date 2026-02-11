package com.eventhive.shared.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record OrderCreatedEvent(
    @JsonProperty("orderId") UUID orderId,
    @JsonProperty("customerId") String customerId,
    @JsonProperty("amount") BigDecimal amount,
    @JsonProperty("currency") String currency,
    @JsonProperty("items") java.util.List<OrderItemDto> items,
    @JsonProperty("timestamp") Instant timestamp,
    @JsonProperty("idempotencyKey") String idempotencyKey
) {
    @JsonCreator
    public OrderCreatedEvent {}
}
