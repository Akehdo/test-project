package akendo.userservice.controller.dtos;

import akendo.userservice.domain.User;
import org.springframework.data.domain.Page;

import java.util.List;

public record PaginatedUsersResponse(
        List<UserResponse> users,
        int currentPage,
        int totalPages,
        long totalItems,
        int pageSize,
        boolean hasNext,
        boolean hasPrevious
) {
    public static PaginatedUsersResponse from(Page<User> page) {
        return new PaginatedUsersResponse(
                page.getContent().stream()
                        .map(UserResponse::from)
                        .toList(),
                page.getNumber(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.getSize(),
                page.hasNext(),
                page.hasPrevious()
        );
    }
}
