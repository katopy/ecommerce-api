package com.nerdery.ecommerce.persistence.repository;

import com.nerdery.ecommerce.persistence.entity.Order;
import com.nerdery.ecommerce.persistence.entity.Payment;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrder(Order order);

    Optional<Payment> findByStripePaymentId(String id);

}
