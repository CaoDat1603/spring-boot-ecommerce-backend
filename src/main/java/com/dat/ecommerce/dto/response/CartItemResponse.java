package com.dat.ecommerce.dto.response;

import com.dat.ecommerce.entity.CartItem;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CartItemResponse {
    private Long id;
    private Long cartId;
    private Long productId;
    private BigDecimal price;
    private Integer quantity;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public CartItemResponse() {}

    public CartItemResponse(
            Long id,
            Long cartId,
            Long productId,
            BigDecimal price,
            Integer quantity,
            LocalDateTime createAt,
            LocalDateTime updateAt
    ) {
        this.id = id;
        this.cartId = cartId;
        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    public CartItemResponse(CartItem cartItem) {
        this.id = cartItem.getId();
        this.cartId = cartItem.getCart().getId();
        this.productId = cartItem.getProduct().getId();
        this.price = cartItem.getPrice();
        this.quantity = cartItem.getQuantity();
        this.createAt = cartItem.getCreatedAt();
        this.updateAt = cartItem.getUpdatedAt();
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Long getId() {
        return id;
    }

    public Long getCartId() {
        return cartId;
    }

    public Long getProductId() {
        return productId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
