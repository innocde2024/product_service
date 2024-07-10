package com.inno.product.controller;

import com.inno.product.entity.UserDTO;
import com.inno.product.model.Order;
import com.inno.product.model.Product;
import com.inno.product.service.auth.AuthService;
import com.inno.product.service.order.OrderService;
import com.inno.product.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.inno.product.model.OrderItem;
import com.inno.product.service.orderItem.OrderItemService;

import java.util.List;

@RestController
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private AuthService authService;

    @GetMapping("/orders/{orderId}/items")
    public ResponseEntity<?> getAllOrderItemByOrderId(@PathVariable String orderId, @RequestHeader("Authorization") String token) {
        UserDTO userDTO = authService.verifyToken(token);
        Order order = orderService.getOrderById(Integer.parseInt(orderId));
        if(order.getUserId() == userDTO.getId()) {
            List<OrderItem> list = orderItemService.getOrderItemListByOrderId(Integer.parseInt(orderId));
            if (list == null) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok(list);
        } else {
            return new ResponseEntity<>( "Can't access this order",HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/orders/{orderId}/items/{itemId}")
    public ResponseEntity<?> GetOrderItem(@PathVariable String orderId, @PathVariable String itemId, @RequestHeader("Authorization") String token) {
        // handle item of user
        UserDTO userDTO = authService.verifyToken(token);
        Order order = orderService.getOrderById(Integer.parseInt(orderId));
        OrderItem orderItem = orderItemService.getOrderItemById(Integer.parseInt(itemId));
        if(order.getUserId() == userDTO.getId()) {
            if (orderItem == null) {
                return ResponseEntity.badRequest().build();
            } else {
                return ResponseEntity.ok(orderItem);
            }
        } else {
            return new ResponseEntity<>( "Can't access this order",HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/orders/{orderId}/items")
    public ResponseEntity<?> addOrderItem(@PathVariable String orderId, @RequestBody OrderItem orderItem, @RequestHeader("Authorization") String token) {
        UserDTO userDTO = authService.verifyToken(token);
        Order order = orderService.getOrderById(Integer.parseInt(orderId));
        Product product = productService.findById(orderItem.getProduct().getId());
        if(order.getUserId() == userDTO.getId()) {
            if (product.getQuantity() >= orderItem.getQuantity()) {
                double price = product.getPrice() * orderItem.getQuantity();
                orderItem.setOrder(order);
                orderItem.setPrice(price);
                orderItemService.addOrderItem(orderItem);

                product.setQuantity(product.getQuantity() - orderItem.getQuantity());
                System.out.println("quantity: " + product.getQuantity());
                productService.addProduct(product);
                return ResponseEntity.ok(orderItem);
            } else {
                return ResponseEntity.badRequest().body("Not enough item");
            }
        } else {
            return new ResponseEntity<>( "Can't access this order",HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/orders/{orderId}/items/{itemId}")
    public ResponseEntity<?> deleteOrderItem(@PathVariable String orderId, @PathVariable String itemId, @RequestHeader("Authorization") String token) {
        UserDTO userDTO = authService.verifyToken(token);
        Order order = orderService.getOrderById(Integer.parseInt(orderId));
        OrderItem orderItem = orderItemService.getOrderItemById(Integer.parseInt(itemId));
        if(order.getUserId() == userDTO.getId()) {
            if (orderItem == null) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok(orderItemService.deleteOrderItem(orderItem.getOrderItemId()));
        } else {
            return new ResponseEntity<>( "Can't access this order",HttpStatus.FORBIDDEN);
        }
    }

}
