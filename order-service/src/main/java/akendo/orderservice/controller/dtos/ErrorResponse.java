package akendo.orderservice.controller.dtos;

import java.time.Instant;

public record ErrorResponse(
        String message,
        int status,
        String path,
        Instant timestamp
) {
}