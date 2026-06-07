package akendo.notificationservice.messaging.consumer;

import akendo.notificationservice.messaging.events.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
public class OrderConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrderConsumer.class);

    @KafkaListener(
            topics = "order-created",
            containerFactory = "orderCreatedEventKafkaListenerContainerFactory"
    )
    public void consumeOrder(OrderCreatedEvent event){
        log.info("Received order: order={}", event.orderId());
    }
}
