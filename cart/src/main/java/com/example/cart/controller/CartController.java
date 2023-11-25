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
import com.example.cart.service.CartService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;



@Slf4j
@RestController
@RequiredArgsConstructor
public class CartController {
    private final CartRepo cartRepo;
    private final CartService cartService;

    @GetMapping("carts/{cartId}")
    public ResponseEntity<Cart> getCart(@PathVariable Long cartId) {
        var cart = cartRepo.findById(cartId);
        if (cart.isPresent()) {
            log.info("get cart by id {} ", cartId);
            return ResponseEntity.ok(cart.get());
        }
        else {
            log.error("get cart by id {} not found ", cartId);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("carts")
    public ResponseEntity<Cart> createCart(@RequestBody CartRequest request) {
        var user = cartService.getUser(request.getUser());
        if (user.isEmpty()) {
            log.error("Cannot create cart because user does not exits");
            return ResponseEntity.notFound().build();
        }
        var products = cartService.getProducts(request.getProducts().keySet());
        if (products.isEmpty()) {
            log.error("Cannot create cart because products does not exits");
            return ResponseEntity.notFound().build();
        }
        var total = cartService.getTotal(products, request.getProducts());
        var newCart = new Cart(user.get().getId(), products.keySet(), total);
        var cart = cartRepo.save(newCart);
        log.info("created cart {}", cart);
        return ResponseEntity.ok(cart);
    }
}
