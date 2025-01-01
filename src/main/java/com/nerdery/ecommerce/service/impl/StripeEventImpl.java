package com.nerdery.ecommerce.service.impl;

import com.nerdery.ecommerce.persistence.entity.Payment;
import com.nerdery.ecommerce.persistence.repository.PaymentRepository;
import com.nerdery.ecommerce.service.WebhookService;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Charge;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class StripeEventImpl implements WebhookService {

    private final PaymentRepository paymentRepository;

    @Value("${secret.stripe.webhook}")
    String WEBHOOK_KEY;
    @Override
    public void handleEvent(String payload, String signHeader) {
        Event event = verifyEvent(payload, signHeader);
        if (Objects.isNull(event)) {
            throw new IllegalArgumentException("Invalid Stripe Event");}

        switch (event.getType()) {
            case "payment_intent.created":
                handlePaymentSuccess(event);
                break;
            case "charge.succeeded":
                handleChargeSuccess(event);
                break;
            case "payment_intent.failed":
                handlePaymentFailure(event);
                break;

            default:
                System.out.println("Event type still not implemented:  " + event.getType());
                break;
        }
    }

    private void handleChargeSuccess(Event event) {
        Charge charge = (Charge) event.getDataObjectDeserializer()
                .getObject()
                .orElseThrow(() -> new IllegalStateException("Invalid ChargeIntent Payload"));
    }

    private void handlePaymentFailure(Event event) {
        PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer()
                .getObject()
                .orElseThrow(() -> new IllegalStateException("Invalid PaymentIntent payload"));
        Payment existingPayment = paymentRepository.findByStripePaymentId(paymentIntent.getId())
                .orElseThrow(() -> new IllegalStateException("Payment not found for ID: " + paymentIntent.getId()));

        existingPayment.setChargeMadeDate(LocalDateTime.now());
        existingPayment.setPaymentStatus(Payment.PaymentStatus.COMPLETED);
        paymentRepository.save(existingPayment);
    }

    private void handlePaymentSuccess(Event event) {
        PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer()
                .getObject()
                .orElseThrow(() -> new IllegalStateException("Invalid PaymentIntent payload"));

        Payment existingPayment = paymentRepository.findByStripePaymentId(paymentIntent.getId())
                .orElseThrow(() -> new IllegalStateException("Payment not found for ID: " + paymentIntent.getId()));

        existingPayment.setChargeMadeDate(LocalDateTime.now());
        existingPayment.setPaymentStatus(Payment.PaymentStatus.COMPLETED);
        paymentRepository.save(existingPayment);
    }



    private Event verifyEvent(String payload, String signHeader) {
        try {
            return Webhook.constructEvent(payload, signHeader, WEBHOOK_KEY);
        } catch (SignatureVerificationException e) {
            System.err.println("Webhook signature verification failed: " + e.getMessage());
            return null;
        }
    }

}
