package com.dat.ecommerce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StripeCheckoutResponse {

    private Long paymentId;
    private String sessionId;
    private String checkoutUrl;
}