package com.dat.ecommerce.controller;

import com.dat.ecommerce.service.StripeWebhookService;
import com.stripe.exception.StripeException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/webhooks")
public class StripeWebhookController {

    private final StripeWebhookService stripeWebhookService;

    public StripeWebhookController(
            StripeWebhookService stripeWebhookService
    ) {
        this.stripeWebhookService =
                stripeWebhookService;
    }

    @PostMapping("/stripe")
    public ResponseEntity<Void> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature")
            String signature
    ) throws StripeException {

        stripeWebhookService.handleWebhook(
                payload,
                signature
        );

        return ResponseEntity.ok().build();
    }
}