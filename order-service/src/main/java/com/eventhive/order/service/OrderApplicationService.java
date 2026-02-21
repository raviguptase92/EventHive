package com.eventhive.order.service;

import com.eventhive.order.dto.CreateOrderRequest;
import com.eventhive.order.dto.OrderResponse;
import com.eventhive.order.entity.OrderEntity;
import com.eventhive.order.entity.OrderItemEntity;
import com.eventhive.order.repository.OrderRepository;
import com.eventhive.shared.events.OrderCreatedEvent;
import com.eventhive.shared.events.OrderItemDto;
import com.eventhive.shared.events.PaymentRequestedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderApplicationService {

    private final OrderRepository orderRepository;
    private final OrderEventPublisher eventPublisher;

    public OrderApplicationService(OrderRepository orderRepository, OrderEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        // Idempotency: return existing order if the same idempotency key
        var existing = orderRepository.findByIdempotencyKey(request.idempotencyKey());
        if (existing.isPresent()) {
            return toResponse(existing.get());
        }

        BigDecimal total = request.items().stream()
            .map(i -> i.unitPrice().multiply(BigDecimal.valueOf(i.quantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        var order = new OrderEntity();
        order.setCustomerId(request.customerId());
        order.setAmount(total);
        order.setCurrency("INR");
        order.setStatus("CREATED");
        order.setIdempotencyKey(request.idempotencyKey());
        order.setCreatedAt(Instant.now());

        for (var itemReq : request.items()) {
            var item = new OrderItemEntity();
            item.setOrder(order);
            item.setProductId(itemReq.productId());
            item.setQuantity(itemReq.quantity());
            item.setUnitPrice(itemReq.unitPrice());
            order.getItems().add(item);
        }

        order = orderRepository.save(order);
        List<OrderItemDto> itemDtos = order.getItems().stream()
            .map(i -> new OrderItemDto(i.getProductId(), i.getQuantity(), i.getUnitPrice()))
            .toList();

        OrderCreatedEvent orderEvent = new OrderCreatedEvent(
            order.getId(),
            order.getCustomerId(),
            order.getAmount(),
            order.getCurrency(),
            itemDtos,
            order.getCreatedAt(),
            order.getIdempotencyKey()
        );
        eventPublisher.publishOrderCreated(orderEvent);

        PaymentRequestedEvent paymentEvent = new PaymentRequestedEvent(
            UUID.randomUUID(),
            order.getId(),
            order.getAmount(),
            order.getCurrency(),
            order.getCustomerId(),
            order.getCreatedAt(),
            "payment-" + order.getIdempotencyKey()
        );
        eventPublisher.publishPaymentRequested(paymentEvent);
        return toResponse(order);
    }

    private static OrderResponse toResponse(OrderEntity order) {
        var items = order.getItems().stream()
            .map(i -> new com.eventhive.order.dto.OrderItemResponse(
                i.getProductId(), i.getQuantity(), i.getUnitPrice()))
            .toList();
        return new OrderResponse(
            order.getId(),
            order.getCustomerId(),
            order.getAmount(),
            order.getCurrency(),
            order.getStatus(),
            order.getCreatedAt(),
            items
        );
    }
}
