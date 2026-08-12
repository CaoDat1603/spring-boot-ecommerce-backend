package com.dat.ecommerce.dto.response;

import com.dat.ecommerce.entity.Payment;
import com.dat.ecommerce.enums.PaymentMethod;
import com.dat.ecommerce.enums.PaymentProvider;
import com.dat.ecommerce.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentResponse
{
    private Long id;
    private Long orderId;
    private BigDecimal amount;
    private PaymentStatus status;
    private PaymentMethod method;
    private LocalDateTime paidAt;
    private PaymentProvider provider;
    private String providerPaymentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PaymentResponse() { }

    public PaymentResponse(Payment payment) {
        this.id = payment.getId();
        this.orderId = payment.getOrder().getId();
        this.amount = payment.getAmount();
        this.status = payment.getStatus();
        this.method = payment.getMethod();
        this.paidAt = payment.getPaidAt();
        this.provider = payment.getProvider();
        this.providerPaymentId = payment.getProviderPaymentId();
        this.createdAt = payment.getCreatedAt();
        this.updatedAt = payment.getUpdatedAt();
    }

    public Long getId() { return id; }

    public Long getOrderId() { return orderId; }

    public BigDecimal getAmount() { return amount; }

    public PaymentStatus getStatus() { return status; }

    public PaymentMethod getMethod() { return method; }

    public LocalDateTime getPaidAt() { return paidAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
