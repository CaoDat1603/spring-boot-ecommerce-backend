package com.dat.ecommerce.service;

import com.dat.ecommerce.entity.Order;
import com.dat.ecommerce.entity.Payment;
import com.dat.ecommerce.entity.StripeWebhookEvent;
import com.dat.ecommerce.enums.OrderStatus;
import com.dat.ecommerce.enums.PaymentStatus;
import com.dat.ecommerce.repository.PaymentRepository;
import com.dat.ecommerce.repository.StripeWebhookEventRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class StripeWebhookService {

    private final PaymentRepository paymentRepository;
    private final StripeWebhookEventRepository webhookEventRepository;

    @Value("${stripe.webhook-secret}")
    private String webhookSecret;

    public StripeWebhookService(
            PaymentRepository paymentRepository,
            StripeWebhookEventRepository webhookEventRepository
    ) {
        this.paymentRepository = paymentRepository;
        this.webhookEventRepository = webhookEventRepository;
    }

    /**
     * Entry point của Stripe Webhook.
     *
     * payload:
     *      Raw JSON body Stripe gửi tới
     *
     * signature:
     *      Stripe-Signature header
     */
    @Transactional
    public void handleWebhook(
            String payload,
            String signature
    ) throws StripeException {

        /*
         * 1. Verify webhook signature
         *
         * Nếu signature không hợp lệ,
         * Webhook.constructEvent() sẽ throw exception.
         */
        Event event =
                Webhook.constructEvent(
                        payload,
                        signature,
                        webhookSecret
                );

        /*
         * 2. Lấy Stripe Event ID
         * Ví dụ:
         * evt_123456
         */
        String eventId = event.getId();

        /*
         * 3. Webhook Idempotency
         * Stripe có thể gửi cùng một event nhiều lần.
         * Nếu event này đã xử lý rồi
         * → bỏ qua.
         */
        if (webhookEventRepository
                .existsByEventId(eventId)) {

            return;
        }

        /*
         * 4. Lưu event trước khi xử lý business logic
         * Database UNIQUE(event_id)
         * sẽ bảo vệ chúng ta trước concurrent webhook.
         */
        StripeWebhookEvent webhookEvent =
                new StripeWebhookEvent(
                        eventId,
                        event.getType()
                );

        webhookEventRepository.saveAndFlush(
                webhookEvent
        );

        /*
         * 5. Xử lý event
         */
        handleEvent(event);
    }


    /**
     * Xử lý từng loại Stripe event.
     */
    private void handleEvent(Event event) {

        switch (event.getType()) {
            case "checkout.session.completed":
                handleCheckoutCompleted(event);
                break;

            case "checkout.session.expired":
                handleCheckoutExpired(event);
                break;

            default:
                break;
        }
    }


    /**
     * checkout.session.completed
     * Stripe xác nhận Checkout Session đã hoàn thành.
     */
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

        /*
         * Lấy paymentId từ metadata Stripe.
         */
        String paymentId =
                session.getMetadata()
                        .get("paymentId");

        if (paymentId == null) {

            throw new IllegalStateException(
                    "Payment ID missing from Stripe metadata"
            );
        }

        /*
         * Tìm Payment trong database.
         */
        Payment payment =
                paymentRepository
                        .findById(
                                Long.valueOf(paymentId)
                        )
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "Payment not found: "
                                                + paymentId
                                )
                        );

        // Lấy PaymentIntent ID từ Checkout Session
        String paymentIntentId =
                session.getPaymentIntent();

        if (paymentIntentId == null) {
            throw new IllegalStateException(
                    "PaymentIntent ID is missing from Checkout Session"
            );
        }

        // Lưu Stripe PaymentIntent ID
        payment.setProviderPaymentId(
                paymentIntentId
        );

        // Lưu lại Checkout Session ID
        payment.setProviderSessionId(
                session.getId()
        );

        /*
         * Idempotent ở level Payment.
         * Nếu Payment đã PAID,
         * không làm gì nữa.
         */
        if (payment.getStatus() == PaymentStatus.PAID) {
            return;
        }

        /*
         * Payment → PAID
         */
        payment.setStatus(PaymentStatus.PAID);
        payment.setPaidAt(LocalDateTime.now());

        /*
         * Payment → Order
         */
        Order order = payment.getOrder();
        order.setStatus(OrderStatus.CONFIRMED);

        /*
         * Lưu Payment.
         * Order được quản lý bởi JPA
         * thông qua relationship với Payment.
         */
        paymentRepository.save(payment);
    }


    /**
     * checkout.session.expired
     * Checkout Session hết hạn
     * nhưng chưa thanh toán.
     */
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

        /*
         * Lấy paymentId từ metadata.
         */
        String paymentId =
                session.getMetadata()
                        .get("paymentId");

        /*
         * Không có paymentId
         * → không thể xác định Payment.
         */
        if (paymentId == null) {
            return;
        }

        /*
         * Tìm Payment.
         */
        Payment payment =
                paymentRepository
                        .findById(
                                Long.valueOf(paymentId)
                        )
                        .orElse(null);

        if (payment == null) {
            return;
        }

        /*
         * Chỉ chuyển PENDING → CANCELLED.
         * Không được chuyển PAID → CANCELLED.
         */
        if (payment.getStatus() == PaymentStatus.PENDING) {
            payment.setStatus(PaymentStatus.CANCELLED);
            payment.setUpdatedAt(LocalDateTime.now());
            paymentRepository.save(payment);
        }
    }
}