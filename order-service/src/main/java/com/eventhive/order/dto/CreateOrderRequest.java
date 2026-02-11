package com.eventhive.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateOrderRequest(
    @NotBlank String customerId,
    @NotBlank String idempotencyKey,
    @NotEmpty @Valid List<OrderItemRequest> items
) {}
