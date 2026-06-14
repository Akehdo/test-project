package akendo.orderservice.controller;

import akendo.orderservice.controller.dtos.CreateOrderRequest;
import akendo.orderservice.controller.dtos.CreateOrderResponse;
import akendo.orderservice.domain.Order;
import akendo.orderservice.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        Order order = orderService.createOrder(request.customerId(), request.totalAmount());

        return ResponseEntity
                .created(URI.create("/orders/" + order.getId()))
                .body(CreateOrderResponse.from(order));
    }

    @GetMapping
    public List<Order> getOrders() {
        return orderService.getOrders();
    }

    @GetMapping("/{orderId}")
    public Order getOrder(@PathVariable UUID orderId) {
        return orderService.getOrder(orderId);
    }

    @PatchMapping("/{orderId}/pay")
    public Order payOrder(@PathVariable UUID orderId) {
        return orderService.payOrder(orderId);
    }

    @PatchMapping("/{orderId}/cancel")
    public Order cancelOrder(@PathVariable UUID orderId) {
        return orderService.cancelOrder(orderId);
    }


}
