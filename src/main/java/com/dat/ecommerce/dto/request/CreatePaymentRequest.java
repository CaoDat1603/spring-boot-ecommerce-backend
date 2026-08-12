package com.dat.ecommerce.dto.request;

import com.dat.ecommerce.enums.PaymentMethod;

public class CreatePaymentRequest {

    private Long orderId;

    private PaymentMethod method;

    public CreatePaymentRequest() {
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setMethod(PaymentMethod method) {
        this.method = method;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}