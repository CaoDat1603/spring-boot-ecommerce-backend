package com.dat.ecommerce.dto.response;

import com.dat.ecommerce.entity.CartItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponse {

    private Long id;
    private Long productId;
    private String productName;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal subtotal;


    public CartItemResponse(CartItem cartItem) {
        this.id = cartItem.getId();
        this.productId = cartItem.getProduct().getId();
        this.productName = cartItem.getProduct().getName();
        this.price = cartItem.getPrice();
        this.quantity = cartItem.getQuantity();

        this.subtotal = this.price.multiply(
                BigDecimal.valueOf(this.quantity)
        );
    }
}
