package com.nerdery.ecommerce.controller;

import com.nerdery.ecommerce.persistence.entity.Product;
import com.nerdery.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<Page<Product>> findAll(Pageable pageable) {

        Page<Product> productsPage = productService.findAll(pageable);

        if (productsPage.hasContent()) {
            return ResponseEntity.ok(productsPage);
        }
        //return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.notFound().build();

    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> findById(@PathVariable Long productId){
        Optional<Product> product = productService.findById(productId);

        System.out.println("MY PRODUCT   " + product.toString());

        return product.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
        //return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Product>> findByCategory(@PathVariable int categoryId) {
        List<Product> product = productService.getByCategory(categoryId);

        if (product.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
//        //return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        return ResponseEntity.notFound().build();
//    }
        return ResponseEntity.ok(product);
    }

   @PutMapping("/{productId}/disabled")
    public ResponseEntity<Product> disableOneById(@PathVariable Long productId){
        Product product = productService.disableOneById(productId);
        return ResponseEntity.ok(product);
   }


}
