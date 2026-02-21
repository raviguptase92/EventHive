package com.eventhive.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "Request to create a new order")
public record CreateOrderRequest(
    @Schema(description = "Customer identifier", example = "cust-123", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank String customerId,
    @Schema(description = "Idempotency key to prevent duplicate orders on retry", example = "order-key-abc-001", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank String idempotencyKey,
    @Schema(description = "Order line items", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty @Valid List<OrderItemRequest> items
) {}
