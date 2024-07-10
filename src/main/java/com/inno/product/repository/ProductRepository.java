package com.inno.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inno.product.model.Product;

import java.util.List;
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<NamesOnly> findBy();

    interface NamesOnly {
        String getName();
    }
}
