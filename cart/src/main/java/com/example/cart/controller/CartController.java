package com.example.cart.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.cart.domain.Cart;
import com.example.cart.domain.CartRequest;
import com.example.cart.repo.CartRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequiredArgsConstructor
public class CartController {
    private final CartRepo cartRepo;

    @GetMapping("cart/{cartId}")
    public ResponseEntity<Cart> getCart(@PathVariable Long cartId) {
        log.info("get cart by id {} ", cartId);
        var cart = cartRepo.findById(cartId);
        if (cart.isPresent()) {
            return ResponseEntity.ok(cart.get());
        } else {
            log.error("get cart by id {} not found ", cartId);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("cart")
    public ResponseEntity<Cart> createCart(@RequestBody CartRequest request) {
        log.info("create cart for userId {} ", request.getUserId());
        var newUser = new Cart(request.getUserId());
        var user = cartRepo.save(newUser);
        return ResponseEntity.ok(user);
    }
}
