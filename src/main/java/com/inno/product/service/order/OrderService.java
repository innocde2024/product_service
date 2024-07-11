package com.inno.product.service.order;

import com.inno.product.model.Order;

import java.util.List;

public interface OrderService {
    
    List<Order> getOrderList();

    List<Order> getOrderListPersonal(int userId);

    Order getOrderById(int orderId);

    Order addOrder(Order order);

    Order updateOrder(Order order);

    void deleteOrder(int orderId);


}
