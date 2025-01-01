package com.nerdery.ecommerce.dto.products;

import com.nerdery.ecommerce.dto.category.CategoryResponse;

import java.math.BigDecimal;

public record ProductResponse(
        Long productId,
        String name,
        BigDecimal price,
        String details,
        CategoryResponse category

) {
}
