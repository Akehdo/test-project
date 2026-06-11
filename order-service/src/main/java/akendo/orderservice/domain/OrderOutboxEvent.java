package akendo.orderservice.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "order_outbox_events")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderOutboxEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Which business entity produced the event. Here it will usually be "ORDER".
    @Column(name = "aggregate_type", nullable = false, length = 50)
    private String aggregateType;

    // Id of that business entity. For order events this is the order id.
    @Column(name = "aggregate_id", nullable = false)
    private UUID aggregateId;

    // Logical event name that the publisher uses to choose the Kafka topic.
    @Column(name = "event_type", nullable = false, length = 100)
    private String eventType;

    // Serialized event body. Usually JSON of OrderCreatedEvent/OrderPaidEvent/etc.
    @Column(nullable = false, columnDefinition = "TEXT")
    private String payload;

    // PENDING means "saved with the order, but not published to Kafka yet".
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private OrderOutboxStatus status;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Instant updatedAt;

    public static OrderOutboxEvent create(
            String aggregateType,
            UUID aggregateId,
            String eventType,
            String payload
    ) {
        OrderOutboxEvent event = new OrderOutboxEvent();
        event.aggregateType = aggregateType;
        event.aggregateId = aggregateId;
        event.eventType = eventType;
        event.payload = payload;
        event.status = OrderOutboxStatus.PENDING;
        return event;
    }

    public void markSent() {
        status = OrderOutboxStatus.SENT;
    }

    public void markFailed() {
        status = OrderOutboxStatus.FAILED;
    }
}
