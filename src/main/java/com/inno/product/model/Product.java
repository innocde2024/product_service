package com.inno.product.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int quantity;
    private String name;
    private double price;
    @ManyToOne
    @JoinColumn(name="category_id", nullable=false)
    Category category;
    private String description;
    @OneToMany(mappedBy ="product")
    @JsonBackReference
    private List<OrderItem> orderItems;
}
