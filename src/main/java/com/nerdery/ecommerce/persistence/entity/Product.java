package com.nerdery.ecommerce.persistence.entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;
    private String name;
    private BigDecimal price;
    private String details;
    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private int stockNumber;

    @ManyToMany(mappedBy = "likedProducts")
    List<Customer> likes;

    @OneToMany(mappedBy = "product")
    private List<ProductImage> images;

    public enum ProductStatus {
        ENABLED, DISABLED
    }
}
