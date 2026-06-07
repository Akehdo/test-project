package akendo.orderservice.controller.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateOrderRequest(UUID customerId, BigDecimal totalAmount) {
}