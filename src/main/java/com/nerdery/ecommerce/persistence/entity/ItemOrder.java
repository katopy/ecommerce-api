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
@Table(name = "item_order")
public class ItemOrder {
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product productId;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order orderId;

    private int quantityProduct;
    private BigDecimal totalProduct;
}
