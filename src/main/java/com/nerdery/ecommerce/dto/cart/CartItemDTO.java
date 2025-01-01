package com.nerdery.ecommerce.dto.cart;

import com.nerdery.ecommerce.persistence.entity.Cart;
import com.nerdery.ecommerce.persistence.entity.Customer;
import com.nerdery.ecommerce.persistence.entity.Product;

public record CartItemDTO(
        Product product,
        Long customer,
        int quantity
) {
}
