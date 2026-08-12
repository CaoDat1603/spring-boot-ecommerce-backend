package com.dat.ecommerce.dto.response;

import com.dat.ecommerce.entity.OrderItem;

import java.math.BigDecimal;

public class OrderItemResponse {
    private Long id;
    private Long productId;
    private String productName;
    private BigDecimal productPrice;
    private Integer quantity;
    private BigDecimal subtotal;
    public OrderItemResponse() { }

    public OrderItemResponse(OrderItem orderItem) {
        this.id = orderItem.getId();
        this.productId = orderItem.getProduct().getId();
        this.productName = orderItem.getProductName();
        this.productPrice = orderItem.getProductPrice();
        this.quantity = orderItem.getQuantity();
        this.subtotal = orderItem.getSubtotal();
    }
    public Long getId() { return id; }

    public Long getProductId() { return productId; }

    public String getProductName() { return productName; }

    public BigDecimal getProductPrice() { return productPrice; }

    public Integer getQuantity() { return quantity; }

    public BigDecimal getSubtotal() { return subtotal; }
}
