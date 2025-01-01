package com.nerdery.ecommerce.service.impl;

import com.nerdery.ecommerce.dto.order.OrderRequest;
import com.nerdery.ecommerce.exception.ObjectNotFoundException;
import com.nerdery.ecommerce.persistence.entity.*;
import com.nerdery.ecommerce.persistence.repository.*;
import com.nerdery.ecommerce.service.OrderService;
import com.nerdery.ecommerce.service.UserService;
import com.nerdery.ecommerce.service.auth.AuthenticationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Window;
import org.springframework.graphql.data.query.ScrollSubrange;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserService userService;
    private final CustomerRepository customerRepository;
    private final AuthenticationService authenticationService;
    private final CartItemRepository cartItemRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    @Override
    public List<Order> getOrdersByCustomer(Long id) {
        System.out.println("MY ORDER" + orderRepository.getOrderByCustomer(id));
        return orderRepository.getOrderByCustomer(id);
    }

    @Override
    public Page<Order> getPaginatedOrders(int first, String after) {
        return null;
    }

    @Override
    public Window<Order> getMyOrders(ScrollSubrange subrange) {
        Long userId = userService.getAuthenticatedUserId();
        System.out.println("User: " + 7);
        ScrollPosition position = subrange.position().orElse(ScrollPosition.offset());
        Limit limit = Limit.of(subrange.count().orElse(10));
        return orderRepository.findByCustomer_UserId_UserId(userId, position, limit);
    }

    @Override
    @Transactional
    public Order createOrderFromCart(List<OrderRequest> orderRequest) {
        System.out.println(orderRequest + "CART ITEMS");

        //User loggedInUser = authenticationService.findLoggedInUser();
        Customer customer = customerRepository.findByCustomerId(9L);

        if (customer == null) {
            throw new IllegalArgumentException("Customer not found for the logged-in user.");
        }
        List<CartItem> cartItems = cartItemRepository.findByCartCustomer(customer);
        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Cart is empty. Cannot create an order.");
        }

        Order order = new Order();
        order.setCustomer(customer);
        order.setStatus(Order.OrderStatus.IN_PROGRESS);
        BigDecimal totalOrderAmount = BigDecimal.ZERO;
        List<ItemOrder> itemOrders = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            ItemOrder itemOrder = new ItemOrder();
            itemOrder.setOrderId(order);
            itemOrder.setProductId(cartItem.getProduct());
            itemOrder.setQuantityProduct(cartItem.getQuantity());

            BigDecimal itemTotal = cartItem.getProduct()
                    .getPrice()
                    .multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            itemOrder.setTotalProduct(itemTotal);
            itemOrders.add(itemOrder);
            Product product = cartItem.getProduct();
            int updatedStock = product.getStockNumber() - cartItem.getQuantity();
            if (updatedStock < 0) {
                throw new IllegalStateException("Insufficient stock for product: " + product.getName());
            }
            product.setStockNumber(updatedStock);
            productRepository.save(product);
            totalOrderAmount = totalOrderAmount.add(itemTotal);
        }

        order.setItems(itemOrders);

        System.out.println(itemOrders + "THE LIST OF ITEMS IN ORDER");

        orderItemRepository.saveAll(itemOrders);
        order.setTotal(totalOrderAmount);
        orderRepository.save(order);

        cartItemRepository.deleteAll(cartItems);

        return order;
    }


}
