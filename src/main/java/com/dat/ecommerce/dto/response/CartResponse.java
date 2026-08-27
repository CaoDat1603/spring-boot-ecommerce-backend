package com.dat.ecommerce.dto.response;

import com.dat.ecommerce.entity.Cart;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CartResponse {

    private Long id;
    private Long userId;
    private List<CartItemResponse> items;
    private BigDecimal totalAmount;


    public CartResponse(
            Cart cart,
            List<CartItemResponse> items
    ) {
        this.id = cart.getId();
        this.userId = cart.getUser().getId();
        this.items = items;

        this.totalAmount = items.stream()
                .map(CartItemResponse::getSubtotal)
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );
    }

}
