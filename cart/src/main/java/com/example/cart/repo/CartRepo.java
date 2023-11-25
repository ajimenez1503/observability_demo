package com.example.cart.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cart.domain.Cart;



public interface CartRepo extends JpaRepository<Cart, Long> {
}
