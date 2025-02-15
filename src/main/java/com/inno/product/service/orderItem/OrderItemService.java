package com.inno.product.service.orderItem;

import com.inno.product.model.Order;
import com.inno.product.model.OrderItem;

import java.util.List;

public interface OrderItemService {

    List<OrderItem> getOrderItemList();

    List<OrderItem> getOrderItemListByOrderId(int id);

    OrderItem getOrderItemById(int orderId);

    void addOrderItem(OrderItem orderItem);

    OrderItem updateOrderItem(OrderItem orderItem);

    boolean deleteOrderItem(int orderItemId);
    double calculatePrice(List<OrderItem> orderItems);

    OrderItem createOrderItemByProductIdAndQuantity(int productId, int quantity, Order order);
}
