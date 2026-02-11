package com.eventhive.shared.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PaymentCompletedEvent(
    @JsonProperty("paymentId") UUID paymentId,
    @JsonProperty("orderId") UUID orderId,
    @JsonProperty("amount") BigDecimal amount,
    @JsonProperty("status") String status,
    @JsonProperty("transactionId") String transactionId,
    @JsonProperty("timestamp") Instant timestamp
) {
    @JsonCreator
    public PaymentCompletedEvent {}
}
