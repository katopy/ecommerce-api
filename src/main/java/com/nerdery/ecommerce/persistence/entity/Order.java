package com.nerdery.ecommerce.persistence.entity;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "orders")
public class Order implements Serializable  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "orderId", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ItemOrder> items;

    public enum OrderStatus{
        SUCCEEDED, IN_PROGRESS, FAILED
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", total=" + total +
                ", status=" + status +
                ", customerId=" + (customer != null ? customer.getCustomerId() : "null") +
                ", itemsCount=" + (items != null ? items.size() : 0) +
                '}';
    }

}
