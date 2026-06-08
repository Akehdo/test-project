package akendo.orderservice.domain;

import akendo.orderservice.exceptions.BadRequestException;
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
import org.hibernate.annotations.CreationTimestamp;
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
    private UUID id;

    @Column(nullable = false)
    private UUID customerId;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private OrderStatus status;

    private Instant paidAt;

    private Instant cancelledAt;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;

    public static Order create(UUID customerId, BigDecimal totalAmount) {
        if (customerId == null) {
            throw new BadRequestException("customerId must not be null");
        }
        if (totalAmount == null || totalAmount.signum() <= 0) {
            throw new BadRequestException("totalAmount must be positive");
        }

        Order order = new Order();
        order.id = UUID.randomUUID();
        order.customerId = customerId;
        order.totalAmount = totalAmount;
        order.status = OrderStatus.CREATED;

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
