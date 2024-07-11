package com.inno.product.service.category;

import com.inno.product.model.Category;

import java.util.List;

public interface CategoryService {

    List<Category> getAllCategory();

    Category getCategorybyId(int categoryId);

    Category addCategory(Category category);

    boolean deleteCategory(int id);
}
