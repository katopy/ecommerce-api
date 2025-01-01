package com.nerdery.ecommerce.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
@Entity
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String stripePaymentId;
    private String stripeCustomerId;
    @ManyToOne
    private Customer customer;
    @OneToOne
    private Order order;

    private BigDecimal totalAmount;

    private LocalDateTime createdAt;

    private LocalDateTime chargeMadeDate;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    public enum PaymentStatus{
        COMPLETED, FAILED, IN_PROGRESS
    }


}
