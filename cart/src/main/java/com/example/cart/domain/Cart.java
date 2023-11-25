package com.example.cart.domain;

import java.util.Map;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;



@Entity
@Data
@NoArgsConstructor
@Table(name = "PERSON")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long userId;
    @ElementCollection
    private Map<Long, Long> productIdsAndAmount;
    private double total;

    public Cart(Long userId, Map<Long, Long> productIdsAndAmount, double total) {
        this.userId = userId;
        this.productIdsAndAmount = productIdsAndAmount;
        this.total = total;
    }
}
