package com.nerdery.ecommerce.controller.rest;

import com.nerdery.ecommerce.dto.cart.CartItemDTO;
import com.nerdery.ecommerce.dto.cart.SaveItem;
import com.nerdery.ecommerce.persistence.entity.CartItem;
import com.nerdery.ecommerce.service.impl.CartServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping("/cart")
public class CartController {
    private final CartServiceImpl cartService;
    @GetMapping
    public ResponseEntity<List<CartItemDTO>> getMyCartItems(){
        List<CartItemDTO> cart = cartService.getCartByCustomerId();
        return ResponseEntity.ok(cart);
    }
    @PutMapping("/cart/items")
    public ResponseEntity<List<CartItem>> addItemToCart(@RequestBody List<SaveItem> newItems){
        List<CartItem> cartItems = cartService.addItemToCart(newItems);
        return ResponseEntity.ok(cartItems);
    }
}
