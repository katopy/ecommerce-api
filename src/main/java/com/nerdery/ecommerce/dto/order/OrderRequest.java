package com.nerdery.ecommerce.dto.order;

public record OrderRequest(
         Long productId,
         Integer quantity

) {
}
