package com.example.cart.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;



@Data
@RequiredArgsConstructor
public class Product {
    private Long id;
    private String name;
    private Double price;
    private String description;
    private Category category;
}
