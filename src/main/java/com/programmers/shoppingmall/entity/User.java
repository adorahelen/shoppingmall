package com.programmers.shoppingmall.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   // @OneToMany
    private Long id;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "user")
    private List<Review> reviews;

}
