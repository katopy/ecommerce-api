package com.nerdery.ecommerce.persistence.entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
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
    Set<Customer> likes;

//    @OneToMany(mappedBy = "product")
//    private List<ProductImage> images;

    public enum ProductStatus {
        ENABLED, DISABLED
    }
    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", status=" + status +
                ", category=" + category +
                ", stockNumber=" + stockNumber +
                ", likes=" + likes +
                '}';
    }
}
