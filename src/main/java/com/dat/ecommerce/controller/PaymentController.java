package com.dat.ecommerce.controller;

import com.dat.ecommerce.dto.request.PaymentFilterRequest;
import com.dat.ecommerce.dto.response.PaymentResponse;
import com.dat.ecommerce.dto.response.StripeCheckoutResponse;
import com.dat.ecommerce.enums.PaymentMethod;
import com.dat.ecommerce.enums.PaymentStatus;
import com.dat.ecommerce.service.PaymentService;
import com.stripe.exception.StripeException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/stripe")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
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
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
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

    @GetMapping("/order/{orderId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
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

    @GetMapping()
    public ResponseEntity<Page<PaymentResponse>> getPayments(
            Authentication authentication,

            @RequestParam(required = false)
            PaymentStatus status,

            @RequestParam(required = false)
            Long userId,

            @RequestParam(required = false)
            PaymentMethod method,

            @RequestParam(required = false)
            BigDecimal minAmount,

            @RequestParam(required = false)
            BigDecimal maxAmount,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime createdFrom,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime createdTo,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime paidFrom,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime paidTo,

            @ParameterObject
            @PageableDefault(
                    size = 10,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            )
            Pageable pageable
    ) {
        PaymentFilterRequest filter = new PaymentFilterRequest();

        filter.setPaymentStatus(status);
        filter.setUserId(userId);
        filter.setPaymentMethod(method);
        filter.setMaxAmount(maxAmount);
        filter.setMinAmount(minAmount);
        filter.setCreatedFrom(createdFrom);
        filter.setCreatedTo(createdTo);
        filter.setPaidFrom(paidFrom);
        filter.setPaidTo(paidTo);

        return ResponseEntity.ok(
                paymentService.getPayments(
                        authentication.getName(),
                        filter,
                        pageable
                )
        );
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<List<PaymentResponse>> getAll() {
        return ResponseEntity.ok(
                paymentService.getAlPayment()
        );
    }
}