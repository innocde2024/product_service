package com.inno.product.service.orderItem;

import com.inno.product.exception.ApiRequestException;
import com.inno.product.model.Order;
import com.inno.product.model.OrderItem;
import com.inno.product.model.Product;
import com.inno.product.repository.OrderItemRepository;
import com.inno.product.service.order.OrderService;
import com.inno.product.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<OrderItem> getOrderItemListByOrderId(int orderId) {
        return orderItemRepository.getOrderItemsByOrder_OrderId(orderId);
    }

    @Override
    public double calculatePrice(List<OrderItem> orderItems) {
        double price = 0;
        for (OrderItem orderItem : orderItems) {
            price+=orderItem.getProduct().getPrice()*orderItem.getQuantity();
        }
        return price;
    }

    @Override
    public OrderItem createOrderItemByProductIdAndQuantity(int productId, int quantity, Order order) {
        Product product = productService.findById(productId);
        if(product.getQuantity() >= quantity) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(quantity);
            orderItem.setOrder(order);
            orderItem.setPrice(product.getPrice() * quantity);
            orderItemRepository.save(orderItem);
            product.setQuantity(product.getQuantity() - quantity);
            productService.addProduct(product);
            return orderItem;
        } else throw new ApiRequestException("bad_request", HttpStatus.BAD_REQUEST);
    }
}
