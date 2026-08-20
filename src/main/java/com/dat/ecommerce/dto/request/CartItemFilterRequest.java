package com.dat.ecommerce.dto.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CartItemFilterRequest {
    private Long productId;

    private String namePorduct;

    private String sku;

    private Integer quantityMin;

    private Integer quantityMax;

    private BigDecimal priceMin;

    private BigDecimal priceMax;

    private LocalDateTime createdFrom;

    private LocalDateTime createdTo;

    public Long getProductId() {
        return productId;
    }

    public String getSku() {
        return sku;
    }

    public Integer getQuantityMin() {
        return quantityMin;
    }

    public BigDecimal getPriceMax() {
        return priceMax;
    }

    public BigDecimal getPriceMin() {
        return priceMin;
    }

    public Integer getQuantityMax() {
        return quantityMax;
    }

    public LocalDateTime getCreatedTo() {
        return createdTo;
    }

    public LocalDateTime getCreatedFrom() {
        return createdFrom;
    }

    public String getNamePorduct() {
        return namePorduct;
    }

    public void setCreatedTo(LocalDateTime createdTo) {
        this.createdTo = createdTo;
    }

    public void setCreatedFrom(LocalDateTime createdFrom) {
        this.createdFrom = createdFrom;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public void setNamePorduct(String namePorduct) {
        this.namePorduct = namePorduct;
    }

    public void setPriceMax(BigDecimal priceMax) {
        this.priceMax = priceMax;
    }

    public void setPriceMin(BigDecimal priceMin) {
        this.priceMin = priceMin;
    }

    public void setQuantityMax(Integer quantityMax) {
        this.quantityMax = quantityMax;
    }

    public void setQuantityMin(Integer quantityMin) {
        this.quantityMin = quantityMin;
    }
}
