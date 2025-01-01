package com.nerdery.ecommerce.persistence.repository;

import com.nerdery.ecommerce.persistence.entity.Cart;
import com.nerdery.ecommerce.persistence.entity.CartItem;
import com.nerdery.ecommerce.persistence.entity.Customer;
import com.nerdery.ecommerce.persistence.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCart_Customer_UserId_UserId(Long userId);

    Optional<CartItem> findByCartAndProduct(Cart customerCart, Product product);

    List<CartItem> findByCartCustomer(Customer customer);
}
