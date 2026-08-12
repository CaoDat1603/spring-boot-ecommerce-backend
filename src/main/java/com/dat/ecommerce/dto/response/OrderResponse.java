package com.dat.ecommerce.dto.response;

import com.dat.ecommerce.entity.Order;
import com.dat.ecommerce.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {
    private Long id;
    private Long userId;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private List<OrderItemResponse> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public OrderResponse() { }

    public OrderResponse(Order order, List<OrderItemResponse> items
    ) {
        this.id = order.getId();
        this.userId = order.getUser().getId();
        this.totalAmount = order.getTotalAmount();
        this.status = order.getStatus();
        this.items = items;
        this.createdAt = order.getCreatedAt();
        this.updatedAt = order.getUpdatedAt();
    }

    public Long getId() { return id; }

    public Long getUserId() { return userId; }

    public BigDecimal getTotalAmount() { return totalAmount; }

    public OrderStatus getStatus() { return status; }

    public List<OrderItemResponse> getItems() { return items; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
