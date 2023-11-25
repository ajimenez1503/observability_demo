package com.example.cart.domain;

import java.util.List;

import lombok.Data;



@Data
public class CartRequest {
    private Long userId;
    private List<Long> productIds;
}
