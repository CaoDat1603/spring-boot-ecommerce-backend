package com.dat.ecommerce.dto.response;

public class StripeCheckoutResponse {

    private Long paymentId;
    private String sessionId;
    private String checkoutUrl;

    public StripeCheckoutResponse() {
    }

    public StripeCheckoutResponse(
            Long paymentId,
            String sessionId,
            String checkoutUrl
    ) {
        this.paymentId = paymentId;
        this.sessionId = sessionId;
        this.checkoutUrl = checkoutUrl;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getCheckoutUrl() {
        return checkoutUrl;
    }
}