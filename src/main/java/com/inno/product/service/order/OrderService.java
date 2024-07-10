package com.inno.product.service.order;

import java.util.List;
import java.util.Map;

import com.inno.product.model.Order;

public interface OrderService {
    
    List<Order> getOrderList();

    List<Order> getOrderListPersonal(int userId);

    Order getOrderById(int orderId);

    void addOrder(Order order);

    Order updateOrder(Order order);

    void deleteOrder(int orderId);

    int calculateQuantity(Order order);

    double calculatePrice(Order order);

}
