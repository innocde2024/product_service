package com.inno.product.service.product;

import com.inno.product.model.Product;

import java.util.List;


public interface ProductService {
    
    List<Product> getAll();

    List<Product> getAllSuggestProduct(int userId);

    Product addProduct(Product product);

    void updateProduct(Product product);

    Product findById(int id);

    boolean deleteProduct(int id);
}
