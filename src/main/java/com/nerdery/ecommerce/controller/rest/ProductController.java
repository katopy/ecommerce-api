package com.nerdery.ecommerce.controller.rest;

import com.nerdery.ecommerce.dto.products.ImagesRequest;
import com.nerdery.ecommerce.dto.products.ProductResponse;
import com.nerdery.ecommerce.dto.products.SaveProduct;
import com.nerdery.ecommerce.persistence.entity.Product;
import com.nerdery.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> findAll(Pageable pageable) {
        Page<ProductResponse> productsPage = productService.findAll(pageable);
        if (productsPage.hasContent()) {
            return ResponseEntity.ok(productsPage);
        }
        return ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasAuthority('CREATE_ONE_PRODUCT')")
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody SaveProduct newProduct) {
        Product product = productService.createOneProduct(newProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> findById(@PathVariable Long productId) {
        Optional<ProductResponse> product = productService.findById(productId);
        return product.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAuthority('READ_ONE_PRODUCT')")
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Product>> findByCategory(@PathVariable int categoryId) {
        List<Product> product = productService.getByCategory(categoryId);

        if (product.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }

    @PreAuthorize("hasAuthority('DISABLE_ONE_PRODUCT')")
    @PutMapping("/{productId}/disabled")
    public ResponseEntity<Product> disableOneById(@PathVariable Long productId) {
        Product product = productService.disableOneById(productId);
        return ResponseEntity.ok(product);
    }

    @PreAuthorize("hasAuthority('UPDATE_ONE_PRODUCT')")
    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateOneById(@PathVariable Long productId, @RequestBody @Valid SaveProduct newProduct) {
        Product product = productService.updateOneById(productId, newProduct);
        return ResponseEntity.ok(product);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{productId}/like")
    public ResponseEntity<String> likeProduct(@PathVariable Long productId) {
        return productService.likeProduct(productId);
    }

    @PreAuthorize("hasAuthority('UPDATE_ONE_PRODUCT')")
    @PostMapping("/{productId}/images")
    public ResponseEntity<String> uploadProductImage(
            @PathVariable Long productId,
            @RequestPart("file") MultipartFile file) throws IOException {

        ImagesRequest request = new ImagesRequest(productId, file);
        productService.uploadProductImage(request);
        return ResponseEntity.ok("Product image uploaded successfully");
    }




}
