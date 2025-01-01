package com.nerdery.ecommerce.persistence.repository;

import com.nerdery.ecommerce.persistence.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductItemRepository extends JpaRepository<ProductImage, Long> {
}

