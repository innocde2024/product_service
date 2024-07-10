package com.inno.product.service.orderItem;

import java.util.List;
import com.inno.product.model.OrderItem;
import com.inno.product.model.Product;

public interface OrderItemService {

    List<OrderItem> getOrderItemList();

    List<OrderItem> getOrderItemListByOrderId(int id);

    OrderItem getOrderItemById(int orderId);

    void addOrderItem(OrderItem orderItem);

    OrderItem updateOrderItem(OrderItem orderItem);

    boolean deleteOrderItem(int orderItemId);
    void returnItems(List<OrderItem> orderItems);
    
}
