package com.inno.product.service.category;

import com.inno.product.model.Category;
import com.inno.product.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategorybyId(int categoryId) {
        return categoryRepository.findById(categoryId).orElse(null);
    }

    @Override
    public Category addCategory(Category category) {
        return categoryRepository.save(category);
    }
    @Override
    public boolean deleteCategory(int id) {
        if (getCategorybyId(id) == null) {
            return false;
        } else {
            categoryRepository.deleteById(id);
            return true;
        }
    }
}
