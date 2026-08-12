package com.dat.ecommerce.service;

import com.dat.ecommerce.entity.Order;
import com.dat.ecommerce.entity.Payment;
import com.dat.ecommerce.enums.OrderStatus;
import com.dat.ecommerce.enums.PaymentStatus;
import com.dat.ecommerce.repository.PaymentRepository;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class StripeWebhookService {

    private final PaymentRepository paymentRepository;

    public StripeWebhookService(
            PaymentRepository paymentRepository
    ) {
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public void handleEvent(Event event) {

        switch (event.getType()) {

            case "checkout.session.completed":
                handleCheckoutCompleted(event);
                break;

            case "checkout.session.expired":
                handleCheckoutExpired(event);
                break;

            default:
                // Những event khác hiện tại chưa cần xử lý
                break;
        }
    }

    private void handleCheckoutCompleted(Event event) {

        Session session =
                (Session) event
                        .getDataObjectDeserializer()
                        .getObject()
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "Unable to deserialize Stripe session"
                                )
                        );

        String paymentId =
                session.getMetadata().get("paymentId");

        if (paymentId == null) {
            throw new IllegalStateException(
                    "Payment ID missing from Stripe metadata"
            );
        }

        Payment payment =
                paymentRepository
                        .findById(Long.valueOf(paymentId))
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "Payment not found: "
                                                + paymentId
                                )
                        );

        // Idempotent:
        // Webhook có thể được gửi lại nhiều lần.
        if (payment.getStatus() == PaymentStatus.PAID) {
            return;
        }

        payment.setStatus(PaymentStatus.PAID);
        payment.setPaidAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());

        Order order = payment.getOrder();

        order.setStatus(OrderStatus.CONFIRMED);

        paymentRepository.save(payment);
    }

    private void handleCheckoutExpired(Event event) {

        Session session =
                (Session) event
                        .getDataObjectDeserializer()
                        .getObject()
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "Unable to deserialize Stripe session"
                                )
                        );

        String paymentId =
                session.getMetadata().get("paymentId");

        if (paymentId == null) {
            return;
        }

        Payment payment =
                paymentRepository
                        .findById(Long.valueOf(paymentId))
                        .orElse(null);

        if (payment == null) {
            return;
        }

        if (payment.getStatus() == PaymentStatus.PENDING) {

            payment.setStatus(PaymentStatus.CANCELLED);
            payment.setUpdatedAt(LocalDateTime.now());

            paymentRepository.save(payment);
        }
    }
}