package com.dat.ecommerce.controller;

import com.dat.ecommerce.dto.request.RegisterRequest;
import com.dat.ecommerce.dto.request.UpdateUserRequest;
import com.dat.ecommerce.dto.request.UserFilterRequest;
import com.dat.ecommerce.dto.response.UserResponse;
import com.dat.ecommerce.enums.Role;
import com.dat.ecommerce.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(
                userService.createUser(request)
        );
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(
                userService.getAllUsers()
        );
    }

    @GetMapping()
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Page<UserResponse>> getUsers(
            Authentication authentication,

            @RequestParam(required = false)
            Long id,

            @RequestParam(required = false)
            String name,

            @RequestParam(required = false)
            String email,

            @RequestParam(required = false)
            Role role,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime createdFrom,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime createdTo,

            @ParameterObject
            @PageableDefault(
                    size = 10,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            )
            Pageable pageable
    ) {
        UserFilterRequest filter = new UserFilterRequest();

        filter.setId(id);
        filter.setName(name);
        filter.setEmail(email);
        filter.setRole(role);
        filter.setCreatedTo(createdTo);
        filter.setCreatedFrom(createdFrom);

        return ResponseEntity.ok(
                userService.getUsers(
                        authentication.getName(),
                        filter,
                        pageable
                )
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(
                userService.getUserById(id)
        );
    }

    @GetMapping("/{email}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(
                userService.getUserByEmail(email)
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(
                userService.updateUser(id, request)
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}
