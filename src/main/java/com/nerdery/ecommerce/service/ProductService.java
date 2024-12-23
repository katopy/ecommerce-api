package com.nerdery.ecommerce.service;

import com.nerdery.ecommerce.dto.SaveProduct;
import com.nerdery.ecommerce.persistence.entity.Product;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    Page<Product> findAll(Pageable pageable);

    Optional<Product> findById(Long id);

    List<Product> getByCategory(Integer id);

    void deleteProduct(Long id);

    Product disableOneById(Long productId);

    Product createOneProduct(SaveProduct newProduct);

    Product updateOneById(Long productId, SaveProduct newProduct);
}
