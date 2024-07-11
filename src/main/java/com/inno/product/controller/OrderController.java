package com.inno.product.controller;

import com.inno.product.entity.OrderDTO;
import com.inno.product.entity.UserDTO;
import com.inno.product.model.Order;
import com.inno.product.model.OrderItem;
import com.inno.product.service.auth.AuthService;
import com.inno.product.service.order.OrderService;
import com.inno.product.service.orderItem.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private AuthService authService;

    @GetMapping("/orders")
    public ResponseEntity<?> getAllOrders() {
        List<Order> list = orderService.getOrderList();
        if(list == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(list);
    }
    @GetMapping("/trees")
    public ResponseEntity<?> getAllTrees() {
        double amount = 0.0;
        List<Order> list = orderService.getOrderList();
        if(list == null) {
            return ResponseEntity.badRequest().build();
        } else {
            for (Order order : list) {
                amount+=orderItemService.calculatePrice(order.getOrderItems());
            }
            return ResponseEntity.ok(amount/50000);
        }
    }
    @GetMapping("/trees/users")
    public ResponseEntity<?> getAllTreesPersonal(@RequestHeader("Authorization") String token) {
        double amount = 0.0;
        UserDTO userDTO = authService.verifyToken(token);
        List<Order> list = orderService.getOrderListPersonal(userDTO.getId());
        if(list == null) {
            return ResponseEntity.badRequest().build();
        } else {
            for (Order order : list) {
                amount+=orderItemService.calculatePrice(order.getOrderItems());
            }
            return ResponseEntity.ok(amount/50000);
        }
    }

    @GetMapping("/orders/users")
    public ResponseEntity<?> getAllOrdersByUser(@RequestHeader("Authorization") String token) {
        UserDTO userDTO = authService.verifyToken(token);
        List<Order> list = orderService.getOrderListPersonal(userDTO.getId());
        if(list == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<?> getOrder(@PathVariable String orderId,@RequestHeader("Authorization") String token) {
        UserDTO userDTO = authService.verifyToken(token);
        List<Order> list = orderService.getOrderListPersonal(userDTO.getId());
        Order order = orderService.getOrderById(Integer.parseInt(orderId));
        if(order == null) {
            return ResponseEntity.badRequest().build();
        } else if (list.contains(order)) {
            return new ResponseEntity<>( "Can't access this order", HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(order);
    }
}
