package com.eventhive.order.web;

import com.eventhive.order.dto.CreateOrderRequest;
import com.eventhive.order.dto.OrderResponse;
import com.eventhive.order.service.OrderApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Orders", description = "Order creation and management")
public class OrderController {

    private final OrderApplicationService orderService;

    public OrderController(OrderApplicationService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "Create an order", description = "Creates a new order. Idempotent: same idempotencyKey returns the existing order without creating a duplicate.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Order created",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request (validation failed)"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        OrderResponse order = orderService.createOrder(request);
        return ResponseEntity
            .created(URI.create("/api/orders/" + order.id()))
            .body(order);
    }
}
