package com.nerdery.ecommerce.persistence.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private String name;
    private BigDecimal price;
    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @OneToMany(mappedBy = "item")
    Set<CartItem> itemRegistration;

    @ManyToOne
    private Category category;

    private int stockNumber;

    @ManyToMany(mappedBy = "likedProducts")
    Set<Customer> likes;

    public enum ProductStatus {
        ENABLED, DISABLED
    }

}
