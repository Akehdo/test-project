package akendo.orderservice.messaging.outbox;

import akendo.orderservice.config.KafkaTopicConfig;
import akendo.orderservice.domain.OrderOutboxEvent;
import akendo.orderservice.domain.OrderOutboxStatus;
import akendo.orderservice.messaging.producer.EventProducer;
import akendo.orderservice.repository.OrderOutboxEventRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderOutboxPublisher {
    private final OrderOutboxEventRepository orderOutboxEventRepository;
    private final EventProducer eventProducer;

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void publishPendingEvents() {
        List<OrderOutboxEvent> events = orderOutboxEventRepository.findByStatus(OrderOutboxStatus.PENDING);

        publishEvents(events);
    }

    @Scheduled(fixedDelay = 30000)
    @Transactional
    public void publishFailedEvents() {
        List<OrderOutboxEvent> events = orderOutboxEventRepository.findByStatus(OrderOutboxStatus.FAILED);

        publishEvents(events);
    }

    private void publishEvents(List<OrderOutboxEvent> events ) {
        for(OrderOutboxEvent event : events) {
            try {
                eventProducer.send(
                        topicByEventType(event.getEventType()),
                        event.getAggregateId().toString(),
                        event.getPayload()
                );

                event.markSent();
            } catch (Exception e) {
                event.markFailed();
            }
        }
    }

    private String topicByEventType(String eventType) {
        return switch (eventType) {
            case "order-created" -> KafkaTopicConfig.ORDER_CREATED_TOPIC;
            case "order-paid" -> KafkaTopicConfig.ORDER_PAID_TOPIC;
            case "order-cancelled" -> KafkaTopicConfig.ORDER_CANCELLED_TOPIC;
            default -> throw new IllegalArgumentException("Unknown event type: " + eventType);
        };
    }
}
