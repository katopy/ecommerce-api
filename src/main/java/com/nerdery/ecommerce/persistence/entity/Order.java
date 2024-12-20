package com.nerdery.ecommerce.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private enum OrderStatus{
        COMPLETED, SHIPPED, IN_PROGRESS, RETURNED, FAILED
    }

}
