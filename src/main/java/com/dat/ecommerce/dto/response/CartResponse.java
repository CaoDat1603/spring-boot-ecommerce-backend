package com.dat.ecommerce.dto.response;

import com.dat.ecommerce.entity.Cart;

import java.time.LocalDateTime;
import java.util.List;

public class CartResponse {
    private Long id;
    private Long userId;
    private List<CartItemResponse> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    CartResponse() {}

    public CartResponse(
            Long id,
            Long userId,
            List<CartItemResponse> items,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.userId = userId;
        this.items = items;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    CartResponse(Cart cart) {
        this.id = cart.getId();
        this.userId = cart.getUser().getId();
        this.createdAt = cart.getCreatedAt();
        this.updatedAt = cart.getUpdatedAt();
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public List<CartItemResponse> getItems() {
        return items;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setItems(List<CartItemResponse> items) {
        this.items = items;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
