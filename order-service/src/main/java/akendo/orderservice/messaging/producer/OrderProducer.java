package akendo.orderservice.messaging.producer;

import akendo.orderservice.config.KafkaTopicConfig;
import akendo.orderservice.messaging.events.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderProducer {
    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    public void sendOrderCreated(OrderCreatedEvent event) {
        kafkaTemplate.send(
                KafkaTopicConfig.ORDER_CREATED_TOPIC,
                event.orderId().toString(),
                event
        );
    }
}
