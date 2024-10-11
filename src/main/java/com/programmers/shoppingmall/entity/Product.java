package com.programmers.shoppingmall.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @OneToMany
    private Long id;

    @Column(unique = true)
    private String name;

    private int price;

//    @ManyToOne
//    private Category category_id;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;


}
