package akendo.orderservice.controller.dtos;

import akendo.orderservice.domain.Order;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        UUID userId,
        BigDecimal totalAmount,
        String status,
        Instant createdAt
) {
    public static OrderResponse from(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getUserId(),
                order.getTotalAmount(),
                order.getStatus().name(),
                order.getCreatedAt()
        );
    }

}
