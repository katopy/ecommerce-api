package com.nerdery.ecommerce.persistence.repository;

import com.nerdery.ecommerce.persistence.entity.ItemOrder;
import com.nerdery.ecommerce.persistence.entity.Order;
import com.nerdery.ecommerce.persistence.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<ItemOrder, Long> {

    List<ItemOrder> getItemOrderByOrderId(Order orderId);

}
