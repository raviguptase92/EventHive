package com.eventhive.order.web;

import com.eventhive.order.dto.CreateOrderRequest;
import com.eventhive.order.dto.OrderResponse;
import com.eventhive.order.service.OrderApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderApplicationService orderService;

    public OrderController(OrderApplicationService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        OrderResponse order = orderService.createOrder(request);
        return ResponseEntity
            .created(URI.create("/api/orders/" + order.id()))
            .body(order);
    }
}
