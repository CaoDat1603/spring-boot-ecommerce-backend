package com.dat.ecommerce.dto.response;

import com.dat.ecommerce.entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponse {
    private Long id;
    private Long productId;
    private String productName;
    private BigDecimal productPrice;
    private Integer quantity;
    private BigDecimal subtotal;


    public OrderItemResponse(OrderItem orderItem) {
        this.id = orderItem.getId();
        this.productId = orderItem.getProduct().getId();
        this.productName = orderItem.getProductName();
        this.productPrice = orderItem.getPriceProduct();
        this.quantity = orderItem.getQuantity();
        this.subtotal = orderItem.getSubtotal();
    }
}
