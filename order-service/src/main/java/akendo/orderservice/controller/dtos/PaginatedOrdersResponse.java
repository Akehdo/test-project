package akendo.orderservice.controller.dtos;

import akendo.orderservice.domain.Order;
import org.springframework.data.domain.Page;

import java.util.List;

public record PaginatedOrdersResponse(
        List<OrderResponse> orders,
        int currentPage,
        int totalPages,
        int totalItems,
        int pageSize,
        boolean hasNext,
        boolean hasPrevious
        ) {

    public static PaginatedOrdersResponse from(Page<Order> page) {
        return new PaginatedOrdersResponse(
                page.getContent().stream()
                        .map(OrderResponse::from)
                        .toList(),
                page.getNumber(),
                page.getTotalPages(),
                page.getNumberOfElements(),
                page.getSize(),
                page.hasNext(),
                page.hasPrevious()
        );
    }
}
