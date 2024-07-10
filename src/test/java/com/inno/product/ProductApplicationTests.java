package com.inno.product;

import com.inno.product.service.order.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductApplicationTests {
@Autowired
	OrderService orderService;
	@Test
	void contextLoads() {
		System.out.println(orderService.calculatePrice(orderService.getOrderById(3)));
	}

}
