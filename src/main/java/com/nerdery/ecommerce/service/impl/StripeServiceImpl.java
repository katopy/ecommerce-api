package com.nerdery.ecommerce.service.impl;

import com.nerdery.ecommerce.dto.payment.ChargeRequest;
import com.nerdery.ecommerce.exception.ObjectNotFoundException;
import com.nerdery.ecommerce.persistence.entity.Customer;
import com.nerdery.ecommerce.persistence.entity.Order;
import com.nerdery.ecommerce.persistence.entity.Payment;
import com.nerdery.ecommerce.persistence.entity.User;
import com.nerdery.ecommerce.persistence.repository.CustomerRepository;
import com.nerdery.ecommerce.persistence.repository.OrderRepository;
import com.nerdery.ecommerce.persistence.repository.PaymentRepository;
import com.nerdery.ecommerce.service.PaymentService;
import com.nerdery.ecommerce.service.auth.AuthenticationService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class StripeServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final AuthenticationService authenticationService;
    private final CustomerRepository customerRepository;

    @Value("${stripe.key}")
    private String SECRET_KEY;

    @PostConstruct
    public void init() {
        Stripe.apiKey = SECRET_KEY;
    }
    @Override
    public PaymentIntent createPaymentIntent(ChargeRequest chargeRequest) throws StripeException {

//        User loggedInUser = authenticationService.findLoggedInUser();
//        String emailUser = loggedInUser.getEmail();
//        Customer customer = customerRepository.findByCustomerId(loggedInUser.getUserId());
//        String customerName = customer.getFirstName().concat(customer.getLastName());

        Order order = orderRepository.findById(chargeRequest.getOrderId()).orElseThrow(() -> new ObjectNotFoundException("Product not found with ID: " + chargeRequest.getOrderId()));

        Optional<Payment> existingPayment = paymentRepository.findByOrder(order);
        if (existingPayment.isPresent()) {
            throw new RuntimeException("Payment available for this order already.") ;
        }
        long cents = order.getTotal().multiply(BigDecimal.valueOf(100)).longValue();
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(cents)
                .setCurrency(chargeRequest.getCurrency().name().toLowerCase())
                .setDescription("Payment for order ID: " + chargeRequest.getOrderId())
                .putMetadata("user-email","Kato@gmail.com")
                .putMetadata("customer-name", "Katherine")
                .build();
        PaymentIntent paymentIntent = PaymentIntent.create(params);
        System.out.println("CUSTOMER " + paymentIntent.getClientSecret());
        Payment paymentDb = new Payment();
        paymentDb.setStripePaymentId(paymentIntent.getId());
        paymentDb.setStripeCustomerId(paymentIntent.getClientSecret());
        //paymentDb.setCustomer(customer);
        paymentDb.setTotalAmount(BigDecimal.valueOf(paymentIntent.getAmount() / 100.0));
        paymentDb.setOrder(order);
        paymentDb.setCreatedAt(LocalDateTime.now());
        paymentDb.setPaymentStatus(Payment.PaymentStatus.IN_PROGRESS);
        paymentRepository.save(paymentDb);
        return paymentIntent;
    }
}
