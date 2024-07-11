package com.inno.product.controller;

import com.inno.product.model.Category;
import com.inno.product.service.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public ResponseEntity<?> getAllCategories() {
        List<Category> list = categoryService.getAllCategory();
        if(list == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<?> getCategoryById(@PathVariable String categoryId) {
        Category category = categoryService.getCategorybyId(Integer.parseInt(categoryId));
        if(category == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(category);
    }

    @PostMapping("/categories")
    public ResponseEntity<?> addCategory(@RequestBody Category category) {
        try {
            Category categoryNew = categoryService.addCategory(category);
            return ResponseEntity.ok(categoryNew);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<?> deleteProduct(@PathVariable String categoryId) {
        return ResponseEntity.ok(categoryService.deleteCategory(Integer.parseInt(categoryId)));
    }
    
}
