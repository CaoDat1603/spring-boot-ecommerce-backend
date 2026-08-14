package com.dat.ecommerce.service;

import com.dat.ecommerce.dto.request.CreatePaymentRequest;
import com.dat.ecommerce.dto.response.PaymentResponse;
import com.dat.ecommerce.dto.response.StripeCheckoutResponse;
import com.dat.ecommerce.entity.*;
import com.dat.ecommerce.enums.IdempotencyStatus;
import com.dat.ecommerce.enums.OrderStatus;
import com.dat.ecommerce.enums.PaymentStatus;
import com.dat.ecommerce.exception.*;
import com.dat.ecommerce.repository.OrderRepository;
import com.dat.ecommerce.repository.PaymentRepository;
import com.dat.ecommerce.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final StripePaymentService stripePaymentService;
    private final IdempotencyService idempotencyService;
    private final ObjectMapper objectMapper;
    private final StripeRefundService stripeRefundService;

    public PaymentService(PaymentRepository paymentRepository, OrderRepository orderRepository, UserRepository userRepository, StripePaymentService stripePaymentService, IdempotencyService idempotencyService, ObjectMapper objectMapper, StripeRefundService stripeRefundService) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.stripePaymentService = stripePaymentService;
        this.idempotencyService = idempotencyService;
        this.objectMapper = objectMapper;
        this.stripeRefundService = stripeRefundService;
    }

    @Transactional
    public StripeCheckoutResponse createStripePayment(
            String email,
            Long orderId,
            String idempotencyKey
    ) throws StripeException {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found"
                        )
                );

        String endpoint = "/api/payments/stripe";

        // 3. Claim Idempotency-Key
        IdempotencyClaim claim =
                idempotencyService.claim(
                        idempotencyKey,
                        user,
                        endpoint
                );

        IdempotencyRecord record =
                claim.record();

        // 4. Nếu key đã thuộc request khác
        if (!claim.owner()) {

            if (record.getStatus()
                    == IdempotencyStatus.COMPLETED) {

                return deserializeResponse(
                        record.getResponseBody()
                );
            }

            if (record.getStatus()
                    == IdempotencyStatus.FAILED) {

                throw new IdempotencyRequestFailedException(
                        record.getResponseStatus(),
                        record.getResponseBody()
                );
            }

            throw new IdempotencyInProgressException(
                    "Request with this Idempotency-Key is currently being processed"
            );
        }

        // CHỈ REQUEST OWNER MỚI ĐƯỢC CHẠY BUSINESS LOGIC
        try {

            Order order = orderRepository
                    .findByIdAndUserId(
                            orderId,
                            user.getId()
                    )
                    .orElseThrow(() ->
                            new OrderNotFoundException(
                                    "Order not found"
                            )
                    );

            if (paymentRepository.findByOrderId(orderId).isPresent()) {
                throw new PaymentAlreadyExistsException(
                        "Payment already exists for order: " + orderId
                );
            }

            StripeCheckoutResponse response =
                    stripePaymentService
                            .createCheckoutSession(
                                    order,
                                    idempotencyKey
                            );

            String responseBody =
                    serializeResponse(response);

            idempotencyService.complete(
                    record,
                    HttpStatus.CREATED.value(),
                    responseBody
            );

            return response;

        } catch (OrderNotFoundException e) {

            idempotencyService.fail(
                    record,
                    HttpStatus.NOT_FOUND.value(),
                    e.getMessage()
            );

            throw e;

        } catch (PaymentAlreadyExistsException e) {

            idempotencyService.fail(
                    record,
                    HttpStatus.CONFLICT.value(),
                    e.getMessage()
            );

            throw e;
        }
    }

    @Transactional
    public PaymentResponse refundPayment(
            String email,
            Long paymentId
    ) throws StripeException, AccessDeniedException {
        Payment payment =
                paymentRepository
                        .findById(paymentId)
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "Payment not found"
                                )
                        );

        Order order = payment.getOrder();

        if (!order.getUser()
                .getEmail()
                .equals(email)) {

            throw new AccessDeniedException(
                    "You do not have permission to refund this payment"
            );
        }

        if (payment.getStatus()
                != PaymentStatus.PAID) {

            throw new IllegalStateException(
                    "Only PAID payment can be refunded"
            );
        }

        if (payment.getProviderPaymentId() == null) {
            throw new IllegalStateException(
                    "Stripe PaymentIntent ID is missing"
            );
        }

        /*
         * 5. Gọi Stripe Refund API
         */
        stripeRefundService.createRefund(payment.getProviderPaymentId());

        payment.setStatus(PaymentStatus.REFUNDED);
        Payment savedPayment = paymentRepository.save(payment);

        return new PaymentResponse(savedPayment);
    }

    @Transactional
    public PaymentResponse confirmPayment(
            String email,
            Long paymentId
    ) {
        Payment payment = paymentRepository
                .findById(paymentId)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Payment not found with id: " + paymentId
                        )
                );

        Order order = payment.getOrder();

        if (!order.getUser().getEmail().equals(email)) {
            throw new IllegalStateException(
                    "You do not have permission to confirm this payment"
            );
        }

        if (payment.getStatus() == PaymentStatus.PAID) {
            return new PaymentResponse(payment);
        }

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new IllegalStateException(
                    "Payment cannot be confirmed"
            );
        }

        payment.setStatus(PaymentStatus.PAID);
        payment.setPaidAt(LocalDateTime.now());

        order.setStatus(OrderStatus.COMPLETED);

        orderRepository.save(order);

        Payment savePayment = paymentRepository.save(payment);

        return new PaymentResponse(savePayment);
    }

    public List<PaymentResponse> getAlPayment() {
        return paymentRepository.findAll()
                .stream()
                .map(PaymentResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public PaymentResponse getPaymentByOrderId(
            String email,
            Long orderId
    ) {
        Order order = orderRepository
                .findByIdAndUserId(
                        orderId,
                        getUserIdByEmail(email)
                ).orElseThrow(() ->
                        new UserNotFoundException(
                                "Order not found with id: " + orderId
                        )
                );

        Payment payment = paymentRepository
                .findByOrderId(order.getId())
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Payment not found"
                        )
                );

        return new PaymentResponse(payment);
    }

    private Long getUserIdByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found with email: " + email)
                ).getId();
    }

    private String serializeResponse(
            StripeCheckoutResponse response
    ) {

        try {

            return objectMapper.writeValueAsString(response);

        } catch (JsonProcessingException e) {

            throw new IllegalStateException(
                    "Failed to serialize payment response",
                    e
            );
        }
    }

    private StripeCheckoutResponse deserializeResponse(
            String responseBody
    ) {

        try {

            return objectMapper.readValue(
                    responseBody,
                    StripeCheckoutResponse.class
            );

        } catch (JsonProcessingException e) {

            throw new IllegalStateException(
                    "Failed to deserialize payment response",
                    e
            );
        }
    }
}
