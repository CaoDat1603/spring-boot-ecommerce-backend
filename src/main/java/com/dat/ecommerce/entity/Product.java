package com.dat.ecommerce.entity;

import com.dat.ecommerce.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
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

    // Optimistic Lock
    @Column(nullable = false)
    @Version
    private Integer version = 0;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


    public Product(
            String name,
            String description,
            BigDecimal price,
            Integer stock,
            String sku,
            ProductStatus status
    ) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.sku = sku;
        this.stock = stock;
        this.status = status;
    }


    // Hibernate sẽ xử lý:
    // INSERT
    //  ↓
    // @PrePersist
    //  ↓
    // createdAt + updatedAt
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    // Hibernate sẽ xử lý:
    // UPDATE
    //  ↓
    // @PreUpdate
    //  ↓
    // updatedAt = thời gian hiện tại
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
