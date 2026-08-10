package com.dat.ecommerce.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

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

    @Min(value = 0, message = "stock cannot be negative")
    private Integer version;

    UpdateProductRequest() {}

    UpdateProductRequest(
            String name,
            String description,
            BigDecimal price,
            Integer stock,
            String sku,
            Integer version
    ) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.sku = sku;
        this.version = version;
    }

    public String getSku() {
        return sku;
    }

    public Integer getVersion() {
        return version;
    }

    public String getName() {
        return name;
    }

    public Integer getStock() {
        return stock;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }
}
