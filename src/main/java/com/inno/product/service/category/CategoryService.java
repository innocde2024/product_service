package com.inno.product.service.category;

import java.util.List;

import com.inno.product.model.Category;

public interface CategoryService {

    List<Category> getAllCategory();

    Category getCategorybyId(int categoryId);

    Category addCategory(Category category);

    boolean deleteCategory(int id);
}
