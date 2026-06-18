package akendo.orderservice.controller;

import akendo.orderservice.controller.dtos.CreateOrderRequest;
import akendo.orderservice.controller.dtos.OrderResponse;
import akendo.orderservice.controller.dtos.PaginatedOrdersResponse;
import akendo.orderservice.domain.Order;
import akendo.orderservice.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Validated
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        Order order = orderService.createOrder(request.customerId(), request.totalAmount());

        return ResponseEntity
                .created(URI.create("/orders/" + order.getId()))
                .body(OrderResponse.from(order));
    }

    @GetMapping
    public PaginatedOrdersResponse getOrders(
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "page must not be negative") int page,
            @RequestParam(defaultValue = "20")
            @Min(value = 1, message = "size must be at least 1")
            @Max(value = 100, message = "size must not exceed 100") int size
    ) {
         Page<Order> orders = orderService.getOrders(page,size);

         return PaginatedOrdersResponse.from(orders);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable UUID orderId) {
         Order order = orderService.getOrder(orderId);

         return ResponseEntity.ok(OrderResponse.from(order));
    }

    @PatchMapping("/{orderId}/pay")
    public ResponseEntity<OrderResponse> payOrder(@PathVariable UUID orderId) {
        Order order = orderService.payOrder(orderId);

        return ResponseEntity.ok(OrderResponse.from(order));
    }

    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable UUID orderId) {
        Order order = orderService.cancelOrder(orderId);

        return ResponseEntity.ok(OrderResponse.from(order));
    }

}
