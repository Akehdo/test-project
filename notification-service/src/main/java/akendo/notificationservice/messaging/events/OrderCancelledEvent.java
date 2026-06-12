package akendo.notificationservice.messaging.events;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record OrderCancelledEvent(
        UUID orderId,
        UUID customerId,
        BigDecimal totalAmount,
        String status,
        Instant cancelledAt
) {
}
