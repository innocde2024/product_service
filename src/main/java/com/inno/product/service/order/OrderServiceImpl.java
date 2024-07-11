package com.inno.product.service.order;

import com.inno.product.model.Order;
import com.inno.product.model.OrderItem;
import com.inno.product.repository.OrderRepository;
import com.inno.product.service.orderItem.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemService orderItemService;

    @Override
    public List<Order> getOrderList() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> getOrderListPersonal(int userId) {
        return orderRepository.getOrdersByUserId(userId);
    }

    @Override
    public Order getOrderById(int orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    @Override
    public Order addOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Order updateOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public void deleteOrder(int orderId) {
        orderRepository.delete(getOrderById(orderId));
    }

}
