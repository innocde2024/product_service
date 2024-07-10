package com.inno.product.controller;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.inno.product.entity.UserDTO;
import com.inno.product.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.inno.product.model.Order;
import com.inno.product.service.order.OrderService;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private AuthService authService;

    @GetMapping("/orders")
    public ResponseEntity<?> getAllOrders(@RequestHeader("Authorization") String token) {
        UserDTO userDTO = authService.verifyToken(token);
        List<Order> list = orderService.getOrderListPersonal(userDTO.getId());
        if(list == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<?> getOrder(@PathVariable String orderId) {
        Order order = orderService.getOrderById(Integer.parseInt(orderId));
        if(order == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(order);
    }

    @PostMapping("/orders")
    public ResponseEntity<?> addOrder(@RequestHeader("Authorization") String token) {
        UserDTO userDTO = authService.verifyToken(token);
        try {
        Order newOrder = new Order();
        newOrder.setOrderDate(new Date());
        newOrder.setUserId(userDTO.getId());
        newOrder.setShipStatus("Pending");
            orderService.addOrder(newOrder);
            return ResponseEntity.ok(newOrder);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
