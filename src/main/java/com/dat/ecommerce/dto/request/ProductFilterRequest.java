package com.dat.ecommerce.dto.request;

import com.dat.ecommerce.enums.ProductStatus;

import java.math.BigDecimal;

public class ProductFilterRequest {
    private String name;
    private String sku;
    private ProductStatus status;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Integer minStock;

    public String getName() {
        return name;
    }

    public String getSku() {
        return sku;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public Integer getMinStock() {
        return minStock;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public void setMinStock(Integer minStock) {
        this.minStock = minStock;
    }
}
