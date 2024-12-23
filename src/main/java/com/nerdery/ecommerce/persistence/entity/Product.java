package com.nerdery.ecommerce.persistence.entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

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

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private int stockNumber;

    @ManyToMany(mappedBy = "likedProducts")
    Set<Customer> likes;

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

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getStockNumber() {
        return stockNumber;
    }

    public void setStockNumber(int stockNumber) {
        this.stockNumber = stockNumber;
    }

    public Set<Customer> getLikes() {
        return likes;
    }

    public void setLikes(Set<Customer> likes) {
        this.likes = likes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return stockNumber == product.stockNumber && Objects.equals(productId, product.productId) && Objects.equals(name, product.name) && Objects.equals(price, product.price) && status == product.status && Objects.equals(category, product.category) && Objects.equals(likes, product.likes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, name, price, status, category, stockNumber, likes);
    }
}
