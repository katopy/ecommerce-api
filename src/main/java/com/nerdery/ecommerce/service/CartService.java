package com.nerdery.ecommerce.service;

import com.nerdery.ecommerce.dto.cart.CartItemDTO;
import com.nerdery.ecommerce.dto.cart.SaveItem;
import com.nerdery.ecommerce.persistence.entity.CartItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CartService {
    List<CartItemDTO> getCartByCustomerId();

    default CartItemDTO toCartDTO(CartItem cart) {
        return new CartItemDTO(
                cart.getProduct(),
                cart.getCart().getCustomer().getCustomerId(),
                cart.getQuantity());
    }
    List<CartItem> addItemToCart(List<SaveItem> newItem);

}
