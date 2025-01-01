package com.nerdery.ecommerce.service;

import com.nerdery.ecommerce.dto.payment.ChargeRequest;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

public interface PaymentService {


    PaymentIntent createPaymentIntent(ChargeRequest chargeRequest) throws StripeException;
}
