package akendo.orderservice.domain;

import akendo.orderservice.exceptions.BadRequestException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "customer_id",nullable = false)
    private UUID customerId;

    @Column(name = "total_amount",nullable = false, precision = 19, scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private OrderStatus status;

    @Version
    private Long version;

    @Column(name = "paid_at")
    private Instant paidAt;

    @Column(name = "cancelled_at")
    private Instant cancelledAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public static Order create(UUID customerId, BigDecimal totalAmount) {
        if (customerId == null) {
            throw new BadRequestException("customerId must not be null");
        }
        if (totalAmount == null || totalAmount.signum() <= 0) {
            throw new BadRequestException("totalAmount must be positive");
        }

        Order order = new Order();
        order.customerId = customerId;
        order.totalAmount = totalAmount;
        order.status = OrderStatus.CREATED;
        order.createdAt = Instant.now();

        return order;
    }

    public void pay() {
        if (status == OrderStatus.CANCELLED) {
            throw new BadRequestException("Cancelled order cannot be paid");
        }

        if (status == OrderStatus.PAID) {
            throw new BadRequestException("Order is already paid");
        }

        status = OrderStatus.PAID;
        paidAt = Instant.now();
    }

    public void cancel() {
        if (status == OrderStatus.PAID) {
            throw new BadRequestException("Paid order cannot be cancelled");
        }

        if (status == OrderStatus.CANCELLED) {
            throw new BadRequestException("Order is already cancelled");
        }

        status = OrderStatus.CANCELLED;
        cancelledAt = Instant.now();
    }
}
