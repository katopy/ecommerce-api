package com.nerdery.ecommerce.service;

import com.nerdery.ecommerce.dto.order.OrderRequest;
import com.nerdery.ecommerce.persistence.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Window;
import org.springframework.graphql.data.query.ScrollSubrange;

import java.util.List;

public interface OrderService {
    List<Order> getOrdersByCustomer(Long id);

    Page<Order> getPaginatedOrders(int first, String after);


    Window<Order> getMyOrders(ScrollSubrange subrange);

    Order createOrderFromCart(List<OrderRequest> orderRequest);
}
