package com.dat.ecommerce.controller;

import com.dat.ecommerce.dto.request.CreatePaymentRequest;
import com.dat.ecommerce.dto.response.PaymentResponse;
import com.dat.ecommerce.dto.response.StripeCheckoutResponse;
import com.dat.ecommerce.service.PaymentService;
import com.stripe.exception.StripeException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // ==========================================
    // CREATE PAYMENT
    // ==========================================

    @PostMapping("/stripe")
    public ResponseEntity<StripeCheckoutResponse> createStripePayment(
            Authentication authentication,
            @RequestHeader("Idempotency-Key")
            String idempotencyKey,
            @RequestParam Long orderId
    ) throws StripeException {

        String email = authentication.getName();

        StripeCheckoutResponse response =
                paymentService.createStripePayment(
                        email,
                        orderId,
                        idempotencyKey
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/{paymentId}/refund")
    public ResponseEntity<PaymentResponse> refundPayment(
            Authentication authentication,
            @PathVariable Long paymentId,
            @RequestHeader("Idempotency-Key")
            String idempotencyKey
    ) throws StripeException, AccessDeniedException {
        String email = authentication.getName();

        PaymentResponse response =
                paymentService.refundPayment(
                        email,
                        paymentId,
                        idempotencyKey
                );

        return ResponseEntity.ok(response);
    }

    // ==========================================
    // CONFIRM PAYMENT
    // ==========================================

    @PostMapping("/{paymentId}/confirm")
    public ResponseEntity<PaymentResponse> confirmPayment(
            Authentication authentication,
            @PathVariable Long paymentId
    ) {

        String email = authentication.getName();

        PaymentResponse response =
                paymentService.confirmPayment(
                        email,
                        paymentId
                );

        return ResponseEntity.ok(response);
    }

    // ==========================================
    // GET PAYMENT BY ORDER
    // ==========================================

    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponse> getPaymentByOrderId(
            Authentication authentication,
            @PathVariable Long orderId
    ) {

        String email = authentication.getName();

        PaymentResponse response =
                paymentService.getPaymentByOrderId(
                        email,
                        orderId
                );

        return ResponseEntity.ok(response);
    }
}