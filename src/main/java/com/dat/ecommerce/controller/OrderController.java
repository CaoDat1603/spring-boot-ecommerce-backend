package com.dat.ecommerce.controller;

import com.dat.ecommerce.dto.request.OrderFilterRequest;
import com.dat.ecommerce.dto.response.OrderResponse;
import com.dat.ecommerce.enums.OrderStatus;
import com.dat.ecommerce.service.OrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<OrderResponse> createOrder(
            Authentication authentication
    ) {
        String email = authentication.getName();
        OrderResponse response = orderService.createOrder(email);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Page<OrderResponse>> getOrders(

            Authentication authentication,

            @RequestParam(required = false)
            OrderStatus status,

            @RequestParam(required = false)
            Long userId,

            @RequestParam(required = false)
            String skuProduct,

            @RequestParam(required = false)
            BigDecimal minTotalAmount,

            @RequestParam(required = false)
            BigDecimal maxTotalAmount,

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
        OrderFilterRequest filter = new OrderFilterRequest();

        filter.setStatus(status);
        filter.setUserId(userId);
        filter.setProductSku(skuProduct);
        filter.setMinTotalAmount(minTotalAmount);
        filter.setMaxTotalAmount(maxTotalAmount);
        filter.setCreatedFrom(createdFrom);
        filter.setCreatedTo(createdTo);

        return ResponseEntity.ok(
                orderService.getOrders(
                        authentication.getName(),
                        filter,
                        pageable
                )
        );
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<OrderResponse>> getMyOrders(
            Authentication authentication
    ) {
        String email = authentication.getName();

        return ResponseEntity.ok(
                orderService.getMyOrders(email)
        );
    }

    @GetMapping("/{orderId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<OrderResponse> getOrderById(
            Authentication authentication,
            @PathVariable Long orderId ) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                orderService.getOrderById( email, orderId )
        );
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<List<OrderResponse>> getOrderById() {
        return ResponseEntity.ok(
                orderService.getAllOrders()
        );
    }
}
