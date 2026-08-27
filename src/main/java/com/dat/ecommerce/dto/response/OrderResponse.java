package com.dat.ecommerce.dto.response;

import com.dat.ecommerce.entity.Order;
import com.dat.ecommerce.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private Long id;
    private Long userId;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private List<OrderItemResponse> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


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
}
