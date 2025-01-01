package com.nerdery.ecommerce.controller.graph;


import com.nerdery.ecommerce.dto.order.OrderRequest;
import com.nerdery.ecommerce.exception.ObjectNotFoundException;
import com.nerdery.ecommerce.persistence.entity.Customer;
import com.nerdery.ecommerce.persistence.entity.ItemOrder;
import com.nerdery.ecommerce.persistence.entity.Order;
import com.nerdery.ecommerce.persistence.repository.CustomerRepository;
import com.nerdery.ecommerce.persistence.repository.OrderItemRepository;
import com.nerdery.ecommerce.persistence.repository.OrderRepository;
import com.nerdery.ecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Window;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.query.ScrollSubrange;
import org.springframework.stereotype.Controller;

import java.awt.*;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class OrderGraphqlController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CustomerRepository customerRepository;

    @QueryMapping
    List<Order> orders(){
        return orderRepository.findAll();
    }

    @QueryMapping
    List<ItemOrder> orderItems(){
        return orderItemRepository.findAll();
    }

    @QueryMapping
    public Window<Order> customerOrders(
            @Argument("clientId") long clientId, ScrollSubrange subrange) {
        Customer customer = customerRepository.findById(clientId)
                .orElseThrow(() -> new ObjectNotFoundException("No found " + clientId));

        System.out.println(customer.getFirstName());

        ScrollPosition scrollPosition = subrange.position().orElse(ScrollPosition.offset());
        Limit limit = Limit.of(subrange.count().orElse(10));

        return orderRepository.findByCustomer(customer, scrollPosition, limit);
    }

    @QueryMapping
    public Window<Order> getMyOrders(ScrollSubrange subrange) {
        return orderService.getMyOrders(subrange);
    }

    @MutationMapping
    public Order buyProducts(@Argument List<OrderRequest> items){
        return orderService.createOrderFromCart(items);
    }


}
