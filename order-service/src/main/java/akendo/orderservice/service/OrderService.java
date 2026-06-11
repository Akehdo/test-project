package akendo.orderservice.service;

import akendo.orderservice.domain.Order;
import akendo.orderservice.domain.OrderOutboxEvent;
import akendo.orderservice.exceptions.OrderNotFoundException;
import akendo.orderservice.messaging.events.OrderCancelledEvent;
import akendo.orderservice.messaging.events.OrderCreatedEvent;
import akendo.orderservice.messaging.events.OrderPaidEvent;
import akendo.orderservice.repository.OrderOutboxEventRepository;
import akendo.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private static final String AGGREGATE_TYPE_ORDER = "Order";

    private static final String EVENT_ORDER_CREATED = "order-created";
    private static final String EVENT_ORDER_PAID = "order-paid";
    private static final String EVENT_ORDER_CANCELLED = "order-cancelled";

    private final OrderRepository orderRepository;
    private final OrderOutboxEventRepository orderOutboxEventRepository;
    private final ObjectMapper objectMapper;

    private Order findOrderByID(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    private String toJson(Object event) {
        return objectMapper.writeValueAsString(event);
    }

    private void saveOutboxEvent(Order order, String eventType, Object payload) {
        OrderOutboxEvent outboxEvent = OrderOutboxEvent.create(
                AGGREGATE_TYPE_ORDER,
                order.getId(),
                eventType,
                toJson(payload)
        );

        orderOutboxEventRepository.save(outboxEvent);
    }

    @Transactional
    public Order createOrder(UUID customerId, BigDecimal totalAmount) {
        Order order = Order.create(customerId, totalAmount);

        orderRepository.save(order);

        OrderCreatedEvent event = new OrderCreatedEvent(
                order.getId(),
                order.getCustomerId(),
                order.getTotalAmount(),
                order.getStatus().name(),
                order.getCreatedAt()
        );

        saveOutboxEvent(
                order,
                EVENT_ORDER_CREATED,
                event
        );
        return order;
    }

    @Transactional(readOnly = true)
    public Order getOrder(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    @Transactional(readOnly = true)
    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order payOrder(UUID orderId) {
        Order order = findOrderByID(orderId);
        order.pay();

        OrderPaidEvent event = new OrderPaidEvent(
                order.getId(),
                order.getCustomerId(),
                order.getTotalAmount(),
                order.getStatus().name(),
                order.getPaidAt()
        );

        saveOutboxEvent(
                order,
                EVENT_ORDER_PAID,
                event
        );

        return order;
    }

    @Transactional
    public Order cancelOrder(UUID orderId) {
        Order order = findOrderByID(orderId);

        order.cancel();

        OrderCancelledEvent event = new OrderCancelledEvent(
                order.getId(),
                order.getCustomerId(),
                order.getTotalAmount(),
                order.getStatus().name(),
                order.getCancelledAt()
        );

        saveOutboxEvent(
                order,
                EVENT_ORDER_CANCELLED,
                event
        );

        return order;
    }
}
