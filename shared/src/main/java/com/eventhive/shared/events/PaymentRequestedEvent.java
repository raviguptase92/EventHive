package com.eventhive.shared.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PaymentRequestedEvent(
    @JsonProperty("paymentId") UUID paymentId,
    @JsonProperty("orderId") UUID orderId,
    @JsonProperty("amount") BigDecimal amount,
    @JsonProperty("currency") String currency,
    @JsonProperty("customerId") String customerId,
    @JsonProperty("timestamp") Instant timestamp,
    @JsonProperty("idempotencyKey") String idempotencyKey
) {
    @JsonCreator
    public PaymentRequestedEvent {}
}
