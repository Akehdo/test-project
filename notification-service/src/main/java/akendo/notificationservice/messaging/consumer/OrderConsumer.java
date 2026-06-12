package akendo.notificationservice.messaging.consumer;

import akendo.notificationservice.messaging.events.OrderCancelledEvent;
import akendo.notificationservice.messaging.events.OrderCreatedEvent;
import akendo.notificationservice.messaging.events.OrderPaidEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
public class OrderConsumer {

   @KafkaListener(
            topics = "order-created",
            containerFactory = "orderCreatedKafkaListenerContainerFactory"
    )
    public void consumeOrderCreated(OrderCreatedEvent event) {
        System.out.println("Order created: " + event);
    }

    @KafkaListener(
            topics = "order-paid",
            containerFactory = "orderPaidKafkaListenerContainerFactory"
    )
    public void consumeOrderPaid(OrderPaidEvent event) {
        System.out.println("Order paid: " + event);
    }

    @KafkaListener(
            topics = "order-cancelled",
            containerFactory = "orderCancelledKafkaListenerContainerFactory"
    )
    public void consumeOrderCancelled(OrderCancelledEvent event) {
        System.out.println("Order cancelled: " + event);
    }
}
