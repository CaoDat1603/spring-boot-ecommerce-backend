package com.dat.ecommerce.entity;

import jakarta.persistence.*;

import javax.print.attribute.standard.MediaSize;
import java.time.LocalDateTime;

@Entity
@Table(name = "idempotency_records")
public class IdempotencyRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "idempotency_key", length = 255)
    private String idempotencyKey;

    @Column(name = "request_path", length = 255)
    private String requestPath;

    @Column(name = "payment_id")
    private Long paymentId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public IdempotencyRecord() {}

    public IdempotencyRecord(
            String idempotencyKey,
            String requestPath,
            Long paymentId
    ) {
        this.idempotencyKey = idempotencyKey;
        this.requestPath = requestPath;
        this.paymentId = paymentId;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public String getRequestPath() {
        return requestPath;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setIdempotencyKey(String idempotencyKey) {
        this.idempotencyKey = idempotencyKey;
    }

    public void setRequestPath(String requestPath) {
        this.requestPath = requestPath;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
