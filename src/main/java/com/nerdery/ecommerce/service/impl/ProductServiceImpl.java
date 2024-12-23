package com.nerdery.ecommerce.service.impl;

import com.nerdery.ecommerce.dto.SaveProduct;
import com.nerdery.ecommerce.exception.ObjectNotFoundException;
import com.nerdery.ecommerce.persistence.entity.Category;
import com.nerdery.ecommerce.persistence.entity.Product;
import com.nerdery.ecommerce.persistence.repository.ProductRepository;
import com.nerdery.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    @Override
    public Page<Product> findAll(Pageable pageable){
        return productRepository.findAll(pageable);
    }

    @Override
    public Optional<Product> findById(Long id){
        return productRepository.findById(id);
    }

    @Override
    public List<Product> getByCategory(Integer id){
        return productRepository.findByCategoryId(id);
    }

    @Override
    public void deleteProduct(Long id) {
         productRepository.deleteById(id);
    }

    @Override
    public Product disableOneById(Long productId){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ObjectNotFoundException("Product not found with id: " + productId));
        product.setStatus(Product.ProductStatus.DISABLED);

        return productRepository.save(product);

    }
    @Override
    public Product createOneProduct(SaveProduct newProduct){
        Product product = new Product();
        product.setName(newProduct.getName());
        product.setPrice(newProduct.getPrice());
        product.setStatus(Product.ProductStatus.ENABLED);
        Category category = new Category();
        category.setCategoryId(newProduct.getCategoryId());
        product.setCategory(category);

        return productRepository.save(product);
    }
    @Override
    public Product updateOneById(Long productId, SaveProduct newProduct){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ObjectNotFoundException("Product not found with id: " + productId));
        product.setName(newProduct.getName());
        product.setPrice(newProduct.getPrice());
        Category category = new Category();
        category.setCategoryId(newProduct.getCategoryId());
        product.setCategory(category);

        return productRepository.save(product);
    }
}
