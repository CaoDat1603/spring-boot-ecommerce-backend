package com.dat.ecommerce.controller;

import com.dat.ecommerce.dto.request.CreatePaymentRequest;
import com.dat.ecommerce.dto.response.PaymentResponse;
import com.dat.ecommerce.service.PaymentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(
            Authentication authentication,
            @RequestBody CreatePaymentRequest request
    ) {

        String email = authentication.getName();

        PaymentResponse response =
                paymentService.createPayment(
                        email,
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
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