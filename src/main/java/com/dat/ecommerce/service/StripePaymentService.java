package com.dat.ecommerce.service;

import com.dat.ecommerce.dto.response.StripeCheckoutResponse;
import com.dat.ecommerce.entity.Order;
import com.dat.ecommerce.entity.Payment;
import com.dat.ecommerce.enums.PaymentMethod;
import com.dat.ecommerce.enums.PaymentProvider;
import com.dat.ecommerce.enums.PaymentStatus;
import com.dat.ecommerce.repository.PaymentRepository;
import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StripePaymentService {

    private final StripeClient stripeClient;
    private final PaymentRepository paymentRepository;

    private final String successUrl;
    private final String cancelUrl;

    public StripePaymentService(
            StripeClient stripeClient,
            PaymentRepository paymentRepository,
            @Value("${stripe.success-url}") String successUrl,
            @Value("${stripe.cancel-url}") String cancelUrl
    ) {
        this.stripeClient = stripeClient;
        this.paymentRepository = paymentRepository;
        this.successUrl = successUrl;
        this.cancelUrl = cancelUrl;
    }

    @Transactional
    public StripeCheckoutResponse createCheckoutSession(
            Order order
    ) throws StripeException {

        Payment payment = new Payment(
                order,
                order.getTotalAmount(),
                PaymentStatus.PENDING,
                PaymentMethod.CARD,
                PaymentProvider.STRIPE
        );

        Payment savedPayment =
                paymentRepository.save(payment);

        SessionCreateParams params =
                SessionCreateParams.builder()
                        .setMode(
                                SessionCreateParams.Mode.PAYMENT
                        )
                        .setSuccessUrl(
                                successUrl
                        )
                        .setCancelUrl(
                                cancelUrl
                        )
                        .addLineItem(
                                SessionCreateParams.LineItem
                                        .builder()
                                        .setQuantity(1L)
                                        .setPriceData(
                                                SessionCreateParams
                                                        .LineItem
                                                        .PriceData
                                                        .builder()
                                                        .setCurrency("vnd")
                                                        .setUnitAmount(
                                                                convertToStripeAmount(
                                                                        order.getTotalAmount()
                                                                )
                                                        )
                                                        .setProductData(
                                                                SessionCreateParams
                                                                        .LineItem
                                                                        .PriceData
                                                                        .ProductData
                                                                        .builder()
                                                                        .setName(
                                                                                "Order #" + order.getId()
                                                                        )
                                                                        .build()
                                                        )
                                                        .build()
                                        )
                                        .build()
                        )
                        .putMetadata(
                                "orderId",
                                order.getId().toString()
                        )
                        .putMetadata(
                                "paymentId",
                                savedPayment.getId().toString()
                        )

                        .build();


        Session session =
                stripeClient.checkout().sessions().create(params);


        savedPayment.setProviderPaymentId(
                session.getId()
        );

        savedPayment.setUpdatedAt(
                java.time.LocalDateTime.now()
        );

        paymentRepository.save(savedPayment);

        return new StripeCheckoutResponse(
                savedPayment.getId(),
                session.getId(),
                session.getUrl()
        );
    }


    private Long convertToStripeAmount(
            java.math.BigDecimal amount
    ) {

        return amount
                .movePointRight(2)
                .longValueExact();
    }
}