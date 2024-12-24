package com.nerdery.ecommerce.controller.rest;
import com.nerdery.ecommerce.persistence.entity.Category;
import com.nerdery.ecommerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;
    @Autowired
    public CategoryController(CategoryService categoryService){this.categoryService = categoryService;}

    @GetMapping
    public ResponseEntity<Page<Category>> findAll(Pageable pageable) {
        Page<Category> categoryPage = categoryService.findAll(pageable);
        if (categoryPage.hasContent()) {
            return ResponseEntity.ok(categoryPage);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category newCategory){
        Category category = categoryService.createOneCategory(newCategory);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }



}
