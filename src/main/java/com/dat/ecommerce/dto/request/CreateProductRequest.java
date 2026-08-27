package com.dat.ecommerce.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class CreateProductRequest {
    @NotBlank(message = "name cannot be blank")
    @Size(max = 255, message = "name cannot exceed 100 characters")
    private String name;

    @Size(max = 500, message = "description cannot exceed 500 characters")
    private String description;

    @NotNull(message = "price is required")
    @DecimalMin(value = "0.01", message = "price must be greater than 0")
    private BigDecimal price;

    @NotNull(message = "stock is required")
    @Min(value = 0, message = "stock cannot be negative")
    private Integer stock;

    @NotBlank(message = "sku is required")
    private String sku;
}
