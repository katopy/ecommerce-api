package com.nerdery.ecommerce.service.impl;

import com.nerdery.ecommerce.dto.category.CategoryResponse;
import com.nerdery.ecommerce.dto.products.ImagesRequest;
import com.nerdery.ecommerce.dto.products.ProductResponse;
import com.nerdery.ecommerce.dto.products.SaveProduct;
import com.nerdery.ecommerce.exception.ObjectNotFoundException;
import com.nerdery.ecommerce.persistence.entity.*;
import com.nerdery.ecommerce.persistence.repository.CustomerRepository;
import com.nerdery.ecommerce.persistence.repository.ProductItemRepository;
import com.nerdery.ecommerce.persistence.repository.ProductRepository;
import com.nerdery.ecommerce.service.ProductService;
import com.nerdery.ecommerce.service.auth.AuthenticationService;
import com.nerdery.ecommerce.service.aws.S3Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final AuthenticationService authenticationService;
    private final CustomerRepository customerRepository;
    private final S3Service s3Service;
    private final ProductItemRepository productItemRepository;

    @Override
    public Page<ProductResponse> findAll(Pageable pageable) {
        Page<Product> productsPage = productRepository.findAll(pageable);
        return productsPage.map(product -> new ProductResponse(
                product.getProductId(),
                product.getName(),
                product.getPrice(),
                product.getDetails(),
                new CategoryResponse(product.getCategory().getCategoryId(), product.getCategory().getName())
        ));
    }

    @Override
    public Optional<ProductResponse> findById(Long id) {
        Optional<Product> productsPage = productRepository.findById(id);
        return productsPage.map(product -> new ProductResponse(
                product.getProductId(),
                product.getName(),
                product.getPrice(),
                product.getDetails(),
                new CategoryResponse(product.getCategory().getCategoryId(), product.getCategory().getName())
        ));
    }

    @Override
    public List<Product> getByCategory(Integer id) {
        return productRepository.findByCategoryId(id);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Product disableOneById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ObjectNotFoundException("Product not found with id: " + productId));
        product.setStatus(Product.ProductStatus.DISABLED);

        return productRepository.save(product);
    }

    @Override
    public Product createOneProduct(SaveProduct newProduct) {
        Product product = new Product();
        product.setName(newProduct.name());
        product.setPrice(newProduct.price());
        product.setStatus(Product.ProductStatus.ENABLED);
        product.setDetails(newProduct.details());
        Category category = new Category();
        category.setCategoryId(newProduct.categoryId());
        product.setCategory(category);

        return productRepository.save(product);
    }

    @Override
    public Product updateOneById(Long productId, SaveProduct newProduct) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ObjectNotFoundException("Product not found with id: " + productId));
        product.setName(newProduct.name());
        product.setPrice(newProduct.price());
        product.setDetails(newProduct.details());
        Category category = new Category();
        category.setCategoryId(newProduct.categoryId());
        product.setCategory(category);

        return productRepository.save(product);
    }

    @Override
    @Transactional
    public ResponseEntity<String> likeProduct(Long productId) {
        User loggedInUser = authenticationService.findLoggedInUser();
        Customer customer = customerRepository.findByUserId(loggedInUser);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found."));
        boolean alreadyLiked = customer.getLikedProducts().contains(product);
        if (alreadyLiked) {
            customer.getLikedProducts().remove(product);
            return ResponseEntity.ok("Product removed from favorites.");
        } else {
            customer.getLikedProducts().add(product);
            return ResponseEntity.ok("Product added to favorites.");
        }
    }

    @Override
    public void uploadProductImage(ImagesRequest imagesRequest) throws IOException{

        Long productId = imagesRequest.productId();
        MultipartFile file = imagesRequest.file();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ObjectNotFoundException("Product not found with the ID: " + productId));

        System.out.println(productId);

        String keyName = "products/" + productId + "/" + file.getName();
        String imageUrl = s3Service.uploadImage(keyName, file);

        System.out.println(imageUrl);


        ProductImage productImage = new ProductImage();
        productImage.setProduct(product);
        productImage.setImageUrl(imageUrl);
        productItemRepository.save(productImage);
    }



}
