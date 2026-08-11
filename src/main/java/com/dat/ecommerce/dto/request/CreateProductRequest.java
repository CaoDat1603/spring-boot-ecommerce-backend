package com.dat.ecommerce.dto.request;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

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

    CreateProductRequest() {}

    CreateProductRequest(
            String name,
            String description,
            BigDecimal price,
            Integer stock,
            String sku
    ) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getStock() {
        return stock;
    }

    public String getSku() {
        return sku;
    }
}
