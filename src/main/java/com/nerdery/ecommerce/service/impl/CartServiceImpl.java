package com.nerdery.ecommerce.service.impl;

import com.nerdery.ecommerce.dto.cart.CartItemDTO;
import com.nerdery.ecommerce.dto.cart.SaveItem;
import com.nerdery.ecommerce.exception.ObjectNotFoundException;
import com.nerdery.ecommerce.persistence.entity.Cart;
import com.nerdery.ecommerce.persistence.entity.CartItem;
import com.nerdery.ecommerce.persistence.entity.Product;
import com.nerdery.ecommerce.persistence.repository.CartItemRepository;
import com.nerdery.ecommerce.persistence.repository.ProductRepository;
import com.nerdery.ecommerce.service.CartService;
import com.nerdery.ecommerce.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartItemRepository cartItemRepository;
    private final UserService userService;
    private final ProductRepository productRepository;

    @Override
    public List<CartItemDTO> getCartByCustomerId() {
        Long userId = userService.getAuthenticatedUserId();
        List<CartItem> cartItems = cartItemRepository.findByCart_Customer_UserId_UserId(userId);
        if (cartItems.isEmpty()) {
            throw new ObjectNotFoundException("User cart not found");
        }
        return cartItems.stream()
                .map(this::toCartDTO)
                .toList();
    }

    @Override
    @Transactional
    public List<CartItem> addItemToCart(List<SaveItem> newItem) {
        //Long userId = userService.getAuthenticatedUserId();
        Cart customerCart = getCartItemsByCustomerUserId(5L);
        return newItem.stream()
                .map(item -> {
                    Product product = productRepository.findById(item.productId())
                            .orElseThrow(() -> new ObjectNotFoundException("Product with ID " + item.productId() + " not found."));
                    CartItem existingCartItem = cartItemRepository.findByCartAndProduct(customerCart, product).orElse(null);
                    if (existingCartItem != null) {
                        existingCartItem.setQuantity(existingCartItem.getQuantity() + item.quantity());
                        existingCartItem.setAddedAt(LocalDateTime.now());
                        return cartItemRepository.save(existingCartItem);
                    } else {
                        CartItem newCartItem = CartItem.builder()
                                .cart(customerCart)
                                .product(product)
                                .quantity(item.quantity())
                                .addedAt(LocalDateTime.now())
                                .build();
                        return cartItemRepository.save(newCartItem);
                    }
                })
                .toList();
    }

    public Cart getCartItemsByCustomerUserId(Long customerUserId) {
        List<CartItem> cartItems = cartItemRepository.findByCart_Customer_UserId_UserId(customerUserId);
        return cartItems.stream()
                .findFirst()
                .map(CartItem::getCart)
                .orElseThrow(() -> new ObjectNotFoundException("There are not items in cart"));
    }


}
