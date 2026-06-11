package akendo.orderservice.repository;

import akendo.orderservice.domain.OrderOutboxEvent;
import akendo.orderservice.domain.OrderOutboxStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderOutboxEventRepository extends JpaRepository<OrderOutboxEvent, UUID> {
    List<OrderOutboxEvent> findByStatus(OrderOutboxStatus status);
}
