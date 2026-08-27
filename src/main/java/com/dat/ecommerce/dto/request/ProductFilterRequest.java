package com.dat.ecommerce.dto.request;

import com.dat.ecommerce.enums.ProductStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ProductFilterRequest {
    private String name;
    private String sku;
    private ProductStatus status;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Integer minStock;
}