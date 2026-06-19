package akendo.userservice.controller;

import akendo.userservice.controller.dtos.CreateUserRequest;
import akendo.userservice.controller.dtos.PaginatedUsersResponse;
import akendo.userservice.controller.dtos.UserResponse;
import akendo.userservice.domain.User;
import akendo.userservice.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        User user = userService.createUser(request.name(), request.email());

        return ResponseEntity
                .created(URI.create("/users/" + user.getId()))
                .body(UserResponse.from(user));
    }

    @GetMapping
    public PaginatedUsersResponse getUsers(
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "page must not be negative") int page,
            @RequestParam(defaultValue = "20")
            @Min(value = 1, message = "size must be at least 1")
            @Max(value = 100, message = "size must not exceed 100") int size
    ) {
        return PaginatedUsersResponse.from(userService.getUsers(page, size));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(UserResponse.from(userService.getUser(userId)));
    }
}
