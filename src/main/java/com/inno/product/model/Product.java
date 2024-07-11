package com.inno.product.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int quantity;
    private String name;
    private double price;
    private String image;
    @ManyToOne
    @JoinColumn(name="category_id", nullable=false)
    Category category;
    private String description;
    @OneToMany(mappedBy ="product")
    @JsonBackReference
    private List<OrderItem> orderItems;
}
