package com.example.cart.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.cart.client.ProductClient;
import com.example.cart.client.UserClient;
import com.example.cart.domain.Product;
import com.example.cart.domain.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;



@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {
    private final UserClient userClient;
    private final ProductClient productClient;

    public Optional<User> getUser(Long userId) {
        try {
            var result = userClient.getUser(userId);
            if (result.getStatusCode().is2xxSuccessful()) {
                return Optional.ofNullable(result.getBody());
            }
            else {
                log.error("Could not find user with id '{}'. Error code {}", userId,
                          result.getStatusCode());
            }

        }
        catch (Exception e) {
            log.error("Could not find user with id '{}'.", userId, e);
        }
        return Optional.empty();
    }

    public List<Product> getProducts(List<Long> productIds) {
        List<Product> products = new ArrayList<>();
        try {
            productIds.forEach(id -> {
                var result = productClient.getProduct(id);
                if (result.getStatusCode().is2xxSuccessful()) {
                    products.add(result.getBody());
                }
                else {
                    log.error("Product with id {} was not found.", id);
                }
            });
        }
        catch (Exception e) {
            log.error("Could not find products with ids '{}'.", productIds, e);
        }
        return products;
    }
}
