package com.inno.product.service.product;

import java.util.List;

import com.inno.product.model.Product;


public interface ProductService {
    
    List<Product> getAll();

    Product addProduct(Product product);

    Product updateProduct(Product product);

    Product findById(int id);

    boolean deleteProduct(int id);
}
