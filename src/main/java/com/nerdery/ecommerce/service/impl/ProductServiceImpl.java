package com.nerdery.ecommerce.service.impl;

import com.nerdery.ecommerce.dto.category.CategoryResponse;
import com.nerdery.ecommerce.dto.products.ProductResponse;
import com.nerdery.ecommerce.dto.products.SaveProduct;
import com.nerdery.ecommerce.exception.ObjectNotFoundException;
import com.nerdery.ecommerce.persistence.entity.Category;
import com.nerdery.ecommerce.persistence.entity.Product;
import com.nerdery.ecommerce.persistence.repository.ProductRepository;
import com.nerdery.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

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
}
