package com.dat.ecommerce.entity;

import com.dat.ecommerce.enums.ProductStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    private String description;

    @Column(nullable = false, precision = 15, scale = 0)
    private BigDecimal price;

    private Integer stock;

    @Column(unique = true, length = 100)
    private String sku;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @Column(nullable = false)
    @Version
    private Integer version;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Product() {}

    public Product(
            String name,
            String description,
            BigDecimal price,
            Integer stock,
            String sku,
            ProductStatus status,
            Integer version
    ) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.sku = sku;
        this.stock = stock;
        this.status = status;
        this.version = version;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getStock() {
        return stock;
    }

    public String getDescription() {
        return description;
    }

    public String getSku() {
        return sku;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public Integer getVersion() {
        return version;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
