package com.dat.ecommerce.entity;

import com.dat.ecommerce.enums.OrderStatus;
import com.dat.ecommerce.enums.PaymentMethod;
import com.dat.ecommerce.enums.PaymentProvider;
import com.dat.ecommerce.enums.PaymentStatus;
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
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @Column(nullable = false, precision = 15, scale = 0)
    private BigDecimal amount;

    @Column(nullable = false, length = 30)
    @Enumerated(value = EnumType.STRING)
    private PaymentStatus status;

    @Column(nullable = false, length = 30)
    @Enumerated(value = EnumType.STRING)
    private PaymentMethod method;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PaymentProvider provider;

    @Column(name = "provider_payment_id")
    private String providerPaymentId;

    @Column(name = "provider_session_id", unique = true)
    private String providerSessionId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


    public Payment(
            Order order,
            BigDecimal amount,
            PaymentStatus status,
            PaymentMethod method,
            PaymentProvider provider
    ) {
        this.order = order;
        this.amount = amount;
        this.status = status;
        this.method = method;
        this.provider = provider;
    }

    public Payment(
            Order order,
            BigDecimal amount,
            PaymentStatus status,
            PaymentMethod method
    ) {
        this.order = order;
        this.amount = amount;
        this.status = status;
        this.method = method;
    }

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
