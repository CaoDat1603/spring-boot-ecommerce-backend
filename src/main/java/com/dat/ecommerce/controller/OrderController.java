package com.dat.ecommerce.controller;

import com.dat.ecommerce.dto.response.OrderResponse;
import com.dat.ecommerce.service.OrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            Authentication authentication
    ) {
        String email = authentication.getName();
        OrderResponse response = orderService.createOrder(email);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getMyOrders(
            Authentication authentication
    ) {
        String email = authentication.getName();

        return ResponseEntity.ok(
                orderService.getMyOrders(email)
        );
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(
            Authentication authentication,
            @PathVariable Long orderId ) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                orderService.getOrderById( email, orderId )
        );
    }
}
