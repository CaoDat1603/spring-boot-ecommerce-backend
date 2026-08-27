package com.dat.ecommerce.dto.response;

import com.dat.ecommerce.entity.Product;
import com.dat.ecommerce.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
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
}