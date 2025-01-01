package com.nerdery.ecommerce.service.impl;

import com.nerdery.ecommerce.exception.ObjectNotFoundException;
import com.nerdery.ecommerce.persistence.entity.Category;
import com.nerdery.ecommerce.persistence.repository.CategoryRepository;
import com.nerdery.ecommerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository){this.categoryRepository = categoryRepository;}

    @Override
    public Page<Category> findAll(Pageable pageable){
        return categoryRepository.findAll(pageable);
    }

    @Override
    public Optional<Category> findById(Long id){
        return categoryRepository.findById(id);
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public Category disableOneById(Long categoryId){
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ObjectNotFoundException("Category not found with id: " + categoryId));
        category.setStatus(Category.CategoryStatus.DISABLED);
        return categoryRepository.save(category);

    }
    @Override
    public Category createOneCategory(Category newCategory){
        Category category = new Category();
        category.setName(newCategory.getName());
        category.setStatus(Category.CategoryStatus.ENABLED);
        return categoryRepository.save(category);
    }
    @Override
    public Category updateOneById(Long categoryId, Category newCategory){
        Category product = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ObjectNotFoundException("Product not found with id: " + categoryId));
        product.setName(newCategory.getName());
        return categoryRepository.save(product);
    }

}
