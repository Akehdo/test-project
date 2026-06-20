package akendo.orderservice.messaging.events;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record OrderPaidEvent(
        UUID orderId,
        UUID userId,
        BigDecimal totalAmount,
        String status,
        Instant paidAt
) {
}
