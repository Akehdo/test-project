package akendo.orderservice.messaging.producer;

import akendo.orderservice.config.KafkaTopicConfig;
import akendo.orderservice.messaging.events.OrderCancelledEvent;
import akendo.orderservice.messaging.events.OrderCreatedEvent;
import akendo.orderservice.messaging.events.OrderPaidEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendOrderCreated(OrderCreatedEvent event) {
        kafkaTemplate.send(
                KafkaTopicConfig.ORDER_CREATED_TOPIC,
                event.orderId().toString(),
                event
        );
    }

    public void sendOrderPaid(OrderPaidEvent event) {
        kafkaTemplate.send(
                KafkaTopicConfig.ORDER_PAID_TOPIC,
                event.orderId().toString(),
                event
        );
    }

    public void sendOrderCancelled(OrderCancelledEvent event) {
        kafkaTemplate.send(
                KafkaTopicConfig.ORDER_CANCELED_TOPIC,
                event.orderId().toString(),
                event
        );
    }
}
