package com.eventhive.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "A single line item in an order")
public record OrderItemRequest(
    @Schema(description = "Product UUID", example = "11111111-1111-1111-1111-111111111111", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull UUID productId,
    @Schema(description = "Quantity", minimum = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @Min(1) int quantity,
    @Schema(description = "Unit price", example = "10.50", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull @DecimalMin("0") BigDecimal unitPrice
) {}
