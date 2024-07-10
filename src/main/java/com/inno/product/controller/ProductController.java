package com.inno.product.controller;

import com.inno.product.entity.UserDTO;
import com.inno.product.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.inno.product.model.Product;
import com.inno.product.service.product.ProductService;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private AuthService authService;

    @GetMapping("/products")
    public ResponseEntity<?> getAll() {
        List<Product> listProducts = productService.getAll();
        if (listProducts == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(listProducts);
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<?> getById(@PathVariable String productId) {
        Product product = productService.findById(Integer.parseInt(productId));
        if (product == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(product);
    }

    @PostMapping("/products")
    public ResponseEntity<?> addProduct(@RequestBody Product product, @RequestHeader("Authorization") String token) {
        UserDTO userDTO = authService.verifyToken(token);
            if (authService.checkRoleAdmin(userDTO)) {
                Product productNew = productService.addProduct(product);
                return ResponseEntity.ok(productNew);
            } else {
                return new ResponseEntity<>("Unable to add product",HttpStatus.FORBIDDEN);
            }
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable String productId, @RequestHeader("Authorization") String token) {
        UserDTO userDTO = authService.verifyToken(token);

        if (authService.checkRoleAdmin(userDTO)) {
            return ResponseEntity.ok(productService.deleteProduct(Integer.parseInt(productId)));
        } else {
            return new ResponseEntity<>("Unable to delete product",HttpStatus.FORBIDDEN);
        }
    }

}
