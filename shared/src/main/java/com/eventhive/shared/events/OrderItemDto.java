package com.eventhive.shared.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record OrderItemDto(
    @JsonProperty("productId") UUID productId,
    @JsonProperty("quantity") int quantity,
    @JsonProperty("unitPrice") java.math.BigDecimal unitPrice
) {
    @JsonCreator
    public OrderItemDto {}
}
