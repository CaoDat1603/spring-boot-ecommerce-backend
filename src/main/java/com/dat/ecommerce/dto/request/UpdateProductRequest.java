package com.dat.ecommerce.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class UpdateProductRequest {
    @Size(max = 255, message = "name cannot exceed 100 characters")
    private String name;

    @Size(max = 500, message = "description cannot exceed 500 characters")
    private String description;

    @DecimalMin(value = "0.01", message = "price must be greater than 0")
    private BigDecimal price;

    @Min(value = 0, message = "stock cannot be negative")
    private Integer stock;

    private String sku;
}
