package com.eventhive.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Schema(description = "Created or existing order")
public record OrderResponse(
    @Schema(description = "Order UUID") UUID id,
    @Schema(description = "Customer identifier") String customerId,
    @Schema(description = "Total amount") BigDecimal amount,
    @Schema(description = "Currency code", example = "USD") String currency,
    @Schema(description = "Order status", example = "CREATED") String status,
    @Schema(description = "Creation timestamp") Instant createdAt,
    @Schema(description = "Order line items") List<OrderItemResponse> items
) {}
