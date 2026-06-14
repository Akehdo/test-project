package akendo.orderservice.controller.dtos;

import akendo.orderservice.domain.Order;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record CreateOrderResponse(
        UUID id,
        UUID customerId,
        BigDecimal totalAmount,
        String status,
        Instant createdAt
) {
    public static CreateOrderResponse from(Order order) {
        return new CreateOrderResponse(
                order.getId(),
                order.getCustomerId(),
                order.getTotalAmount(),
                order.getStatus().name(),
                order.getCreatedAt()
        );
    }

}
