package akendo.notificationservice.messaging.events;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record OrderPaidEvent(
        UUID orderId,
        UUID customerId,
        BigDecimal totalAmount,
        String status,
        Instant paidAt
) {
}
