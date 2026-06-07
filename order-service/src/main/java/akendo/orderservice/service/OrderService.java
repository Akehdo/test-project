package akendo.orderservice.service;

import akendo.orderservice.domain.Order;
import akendo.orderservice.exceptions.OrderNotFoundException;
import akendo.orderservice.messaging.events.OrderCreatedEvent;
import akendo.orderservice.messaging.producer.OrderProducer;
import akendo.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderProducer orderProducer;

    @Transactional
    public Order createOrder(UUID customerId, BigDecimal totalAmount) {
        Order order = Order.create(customerId, totalAmount);

        Order savedOrder = orderRepository.save(order);

        OrderCreatedEvent event = new OrderCreatedEvent(
                savedOrder.getId(),
                savedOrder.getCustomerId(),
                savedOrder.getTotalAmount(),
                savedOrder.getStatus().name()
        );

        orderProducer.sendOrderCreated(event);

        return savedOrder;
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
        Order order = getOrder(orderId);
        order.pay();
        return orderRepository.save(order);
    }

    @Transactional
    public Order cancelOrder(UUID orderId) {
        Order order = getOrder(orderId);
        order.cancel();
        return orderRepository.save(order);
    }
}
