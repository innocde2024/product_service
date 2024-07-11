package com.inno.product;

import com.inno.product.model.Product;
import com.inno.product.service.order.OrderService;
import com.inno.product.service.product.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductApplicationTests {
@Autowired
ProductService productService;
	@Test
	void contextLoads() {
		Product product = productService.findById(1);
		productService.addProduct(product);
	}

}
