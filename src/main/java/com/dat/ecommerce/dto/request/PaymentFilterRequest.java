package com.dat.ecommerce.dto.request;

import com.dat.ecommerce.enums.PaymentMethod;
import com.dat.ecommerce.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentFilterRequest {
    private Long UserId;

    private PaymentStatus paymentStatus;

    private PaymentMethod paymentMethod;

    private BigDecimal minAmount;

    private BigDecimal maxAmount;

    private LocalDateTime createdFrom;

    private LocalDateTime createdTo;

    private LocalDateTime paidFrom;

    private LocalDateTime paidTo;


    public Long getUserId() {
        return UserId;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public BigDecimal getMaxAmount() {
        return maxAmount;
    }

    public BigDecimal getMinAmount() {
        return minAmount;
    }

    public LocalDateTime getCreatedFrom() {
        return createdFrom;
    }

    public LocalDateTime getCreatedTo() {
        return createdTo;
    }

    public LocalDateTime getPaidFrom() {
        return paidFrom;
    }

    public LocalDateTime getPaidTo() {
        return paidTo;
    }

    public void setUserId(Long userId) {
        UserId = userId;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    public void setMaxAmount(BigDecimal maxAmount) {
        this.maxAmount = maxAmount;
    }

    public void setCreatedFrom(LocalDateTime createdFrom) {
        this.createdFrom = createdFrom;
    }

    public void setCreatedTo(LocalDateTime createdTo) {
        this.createdTo = createdTo;
    }

    public void setPaidFrom(LocalDateTime paidFrom) {
        this.paidFrom = paidFrom;
    }

    public void setPaidTo(LocalDateTime paidTo) {
        this.paidTo = paidTo;
    }
}
