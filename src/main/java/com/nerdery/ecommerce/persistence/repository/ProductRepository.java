package com.nerdery.ecommerce.persistence.repository;

import com.nerdery.ecommerce.persistence.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT * FROM products p WHERE p.category_id = :id", nativeQuery = true)
    List<Product> findByCategoryId(@Param("id") Integer id);


}
