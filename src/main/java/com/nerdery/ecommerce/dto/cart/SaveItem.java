package com.nerdery.ecommerce.dto.cart;

import com.nerdery.ecommerce.persistence.entity.Cart;
import com.nerdery.ecommerce.persistence.entity.Customer;
import com.nerdery.ecommerce.persistence.entity.Product;

import java.time.LocalDateTime;

public record SaveItem(

        Long productId,
        Integer quantity
        ) {
}
