package com.dat.ecommerce.dto.request;

import com.dat.ecommerce.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderFilterRequest {

    private OrderStatus status;

    private Long userId;

    private String productSku;

    private BigDecimal minTotalAmount;

    private BigDecimal maxTotalAmount;

    private LocalDateTime createdFrom;

    private LocalDateTime createdTo;

    public OrderStatus getStatus() {
        return status;
    }

    public Long getUserId() {
        return userId;
    }

    public String getProductSku() {
        return productSku;
    }

    public BigDecimal getMinTotalAmount() {
        return minTotalAmount;
    }

    public BigDecimal getMaxTotalAmount() {
        return maxTotalAmount;
    }

    public LocalDateTime getCreatedFrom() {
        return createdFrom;
    }

    public LocalDateTime getCreatedTo() {
        return createdTo;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setUserId(Long orderId) {
        this.userId = orderId;
    }

    public void setProductSku(String productSku) {
        this.productSku = productSku;
    }

    public void setMinTotalAmount(BigDecimal minTotalAmount) {
        this.minTotalAmount = minTotalAmount;
    }

    public void setMaxTotalAmount(BigDecimal maxTotalAmount) {
        this.maxTotalAmount = maxTotalAmount;
    }

    public void setCreatedFrom(LocalDateTime createdFrom) {
        this.createdFrom = createdFrom;
    }

    public void setCreatedTo(LocalDateTime createdTo) {
        this.createdTo = createdTo;
    }
}