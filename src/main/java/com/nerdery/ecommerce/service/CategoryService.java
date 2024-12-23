package com.nerdery.ecommerce.service;

import com.nerdery.ecommerce.persistence.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CategoryService {
    Page<Category> findAll(Pageable pageable);

    Optional<Category> findById(Long id);

    void deleteProduct(Long id);

    Category disableOneById(Long categoryId);

    Category createOneCategory(Category newCategory);

    Category updateOneById(Long categoryId, Category newCategory);
}
