package com.dat.ecommerce.dto.response;

import com.dat.ecommerce.entity.Product;
import com.dat.ecommerce.enums.ProductStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private String sku;
    private ProductStatus status;
    private Integer version;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public ProductResponse() {}
    public ProductResponse(
            Long id,
            String name,
            String description,
            BigDecimal price,
            Integer stock,
            String sku,
            ProductStatus status,
            Integer version,
            LocalDateTime created_at,
            LocalDateTime updated_at
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.sku = sku;
        this.status = status;
        this.version = version;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.stock = product.getStock();
        this.sku = product.getSku();
        this.status = product.getStatus();
        this.version = product.getVersion();
        this.created_at = product.getCreatedAt();
        this.updated_at = product.getUpdatedAt();
    }

    public Long getId() {
        return id;
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

    public String getSku() {
        return sku;
    }

    public Integer getVersion() {
        return version;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public Integer getStock() {
        return stock;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }
}