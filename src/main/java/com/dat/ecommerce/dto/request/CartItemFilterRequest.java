package com.dat.ecommerce.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CartItemFilterRequest {
    private Long productId;

    private String nameProduct;

    private String sku;

    private Integer quantityMin;

    private Integer quantityMax;

    private BigDecimal priceMin;

    private BigDecimal priceMax;

    private LocalDateTime createdFrom;

    private LocalDateTime createdTo;
}
