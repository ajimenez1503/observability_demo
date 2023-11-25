package com.example.cart.domain;

import java.util.Map;

import lombok.Data;



@Data
public class CartRequest {
    private Long user;
    private Map<Long, Long> products;
}
