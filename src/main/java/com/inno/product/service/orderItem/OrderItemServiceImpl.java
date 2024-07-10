package com.inno.product.service.orderItem;

import java.util.List;

import com.inno.product.model.Product;
import com.inno.product.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.inno.product.model.OrderItem;
import com.inno.product.repository.OrderItemRepository;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private ProductService productService;
    @Override
    public List<OrderItem> getOrderItemList() {
        return orderItemRepository.findAll();
    }

    @Override
    public OrderItem getOrderItemById(int orderId) {
        return orderItemRepository.findById(orderId).orElse(null);
    }

    @Override
    public void addOrderItem(OrderItem orderItem) {
        orderItemRepository.save(orderItem);
    }

    @Override
    public OrderItem updateOrderItem(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    @Override
    public boolean deleteOrderItem(int orderItemId) {
        if (getOrderItemById(orderItemId) == null) {
            return false;
        } else {
            orderItemRepository.deleteById(orderItemId);
            return true;
        }
    }

    @Override
    public void returnItems(List<OrderItem> orderItems)  {
        for (OrderItem orderItem: orderItems) {
            Product product = orderItem.getProduct();
            product.setQuantity(product.getQuantity() + orderItem.getQuantity());
            orderItem.setQuantity(0);
            orderItemRepository.save(orderItem);
            productService.updateProduct(product);
        }
    }

    @Override
    public List<OrderItem> getOrderItemListByOrderId(int orderId) {
        return orderItemRepository.getOrderItemsByOrder_OrderId(orderId);
    }
    
}
