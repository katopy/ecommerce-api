package com.nerdery.ecommerce.controller.rest;

import com.nerdery.ecommerce.service.WebhookService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/webhook")
public class WebhookController {

    private final WebhookService webhookService;
    @PostMapping(path = "/stripe")
    public ResponseEntity<String> getEvents(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String signHeader) {
        try {
            webhookService.handleEvent(payload, signHeader);
            return ResponseEntity.ok("Webhook handled successfully");
        }catch (Exception e) {
                e.printStackTrace();  // Log the full stack trace for better debugging
                return ResponseEntity.status(400).body("Webhook handling failed");
            }
    }
}
