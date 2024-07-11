package com.inno.product.service.product;

import com.inno.product.model.Order;
import com.inno.product.model.OrderItem;
import com.inno.product.model.Product;
import com.inno.product.repository.ProductRepository;
import com.inno.product.service.order.OrderService;
import com.inno.product.service.orderItem.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository repository;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemService orderItemService;

    @Override
    public List<Product> getAll() {
        return repository.findAll();
    }

    @Override
    public List<Product> getAllSuggestProduct(int userId) {
        List<Product> products = new ArrayList<>();
        List<Order> orders = orderService.getOrderListPersonal(userId);
        for(Order order : orders) {
            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem orderItem : orderItems) {
                Product product = orderItem.getProduct();
                if (!products.contains(product)) {
                    products.add(product);
                }
            }
        }
        return products;
    }

    @Override
    public Product addProduct(Product product) {
        return repository.save(product);
    }

    @Override
    public void updateProduct(Product product) {
        repository.save(product);
    }

    @Override
    public Product findById(int id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public boolean deleteProduct(int id) {
        if (findById(id) == null) {
            return false;
        } else {
            repository.deleteById(id);
            return true;
        }
    }
}
