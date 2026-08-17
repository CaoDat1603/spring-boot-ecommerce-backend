package com.dat.ecommerce.service;

import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.Refund;
import com.stripe.net.RequestOptions;
import com.stripe.param.RefundCreateParams;
import org.springframework.stereotype.Service;

@Service
public class StripeRefundService {

    private final StripeClient stripeClient;

    public StripeRefundService(
            StripeClient stripeClient
    ) {
        this.stripeClient = stripeClient;
    }

    public Refund createRefund(
            String paymentIntentId,
            String idempotencyKey
    ) throws StripeException {

        RefundCreateParams params =
                RefundCreateParams.builder()
                        .setPaymentIntent(
                                paymentIntentId
                        )
                        .build();

        RequestOptions requestOptions =
                RequestOptions.builder()
                        .setIdempotencyKey(
                                idempotencyKey
                        )
                        .build();



        return stripeClient
                .refunds()
                .create(params,
                        requestOptions
                );
    }
}
