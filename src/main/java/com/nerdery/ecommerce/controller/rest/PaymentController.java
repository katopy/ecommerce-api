package com.nerdery.ecommerce.controller.rest;

import com.nerdery.ecommerce.dto.payment.ChargeRequest;
import com.nerdery.ecommerce.dto.payment.PaymentResponse;
import com.nerdery.ecommerce.service.PaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/checkout")
public class PaymentController {
    private final PaymentService paymentService;

    @PreAuthorize("hasAuthority('CREATE_PAYMENT_INTENT')")
    @PostMapping
    public PaymentResponse createPaymentIntent(@RequestBody ChargeRequest chargeRequest) throws StripeException {
        PaymentIntent paymentIntent = paymentService.createPaymentIntent(chargeRequest);
        return new PaymentResponse(
                paymentIntent.getId(),
                paymentIntent.getAmount(),
                paymentIntent.getCurrency(),
                paymentIntent.getClientSecret()
        );
    }

}
