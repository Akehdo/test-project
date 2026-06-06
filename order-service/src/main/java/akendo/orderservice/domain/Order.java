package akendo.orderservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID customerId;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private OrderStatus status;

    @Column(nullable = false)
    private Instant createdAt;

    public static Order create(UUID customerId, BigDecimal totalAmount) {
        if (customerId == null) {
            throw new IllegalArgumentException("customerId must not be null");
        }
        if (totalAmount == null || totalAmount.signum() <= 0) {
            throw new IllegalArgumentException("totalAmount must be positive");
        }

        Order order = new Order();
        order.id = UUID.randomUUID();
        order.customerId = customerId;
        order.totalAmount = totalAmount;
        order.status = OrderStatus.CREATED;
        order.createdAt = Instant.now();

        return order;
    }

    public void pay() {
        if (status == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Cancelled order cannot be paid");
        }
        status = OrderStatus.PAID;
    }

    public void cancel() {
        if (status == OrderStatus.PAID) {
            throw new IllegalStateException("Paid order cannot be cancelled");
        }
        status = OrderStatus.CANCELLED;
    }
}
