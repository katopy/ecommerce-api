package com.nerdery.ecommerce.controller.graph;

import com.nerdery.ecommerce.dto.cart.SaveItem;
import com.nerdery.ecommerce.persistence.entity.CartItem;
import com.nerdery.ecommerce.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartGraphqlController {

    private final CartService cartService;

    @PreAuthorize("hasAuthority('ADD_ITEMS_CART')")
    @MutationMapping
    public List<CartItem> addItemsCart(@Argument List<SaveItem> items){
        return cartService.addItemToCart(items);
    }
}
