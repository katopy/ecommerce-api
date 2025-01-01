package com.nerdery.ecommerce.persistence.repository;

import com.nerdery.ecommerce.persistence.entity.Customer;
import com.nerdery.ecommerce.persistence.entity.Order;
import com.nerdery.ecommerce.persistence.entity.User;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Window;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import javax.swing.text.html.Option;
import java.awt.*;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value = """
            SELECT o.order_id, o.status, o.total, io.quantity_product, io.total_product, c.first_name
            FROM orders o
            INNER JOIN item_order io on o.order_id = io.order_id
            INNER JOIN customers c on o.customer_id = c.customer_id
            WHERE o.customer_id = :id""", nativeQuery = true)
    List<Order> getOrderByCustomer(@Param("id") Long id);
    Window<Order> findByCustomer(Customer customer, ScrollPosition position, Limit limit);
    Window<Order> findByCustomer_UserId_UserId(Long customer_userId_userId, ScrollPosition position, Limit limit);

}
