package com.inno.product.entity;

import com.inno.product.model.OrderItem;
import lombok.Data;

import java.util.List;
@Data
public class OrderDTO {
    private List<OrderItem> orderItems;
}
