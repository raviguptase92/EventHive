package com.eventhive.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Order line item in response")
public record OrderItemResponse(
    @Schema(description = "Product UUID") UUID productId,
    @Schema(description = "Quantity") int quantity,
    @Schema(description = "Unit price") BigDecimal unitPrice
) {}
