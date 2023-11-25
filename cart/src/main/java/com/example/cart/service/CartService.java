package com.example.cart.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

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

    public Map<Long, Product> getProducts(Collection<Long> productIds) {
        Map<Long, Product> products = new HashMap<>();
        try {
            productIds.forEach(id -> {
                var result = productClient.getProduct(id);
                if (result.getStatusCode().is2xxSuccessful()) {
                    products.put(id, result.getBody());
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

    public Double getTotal(Map<Long, Product> products, Map<Long, Long> productIdsToAmount) {
        AtomicReference<Double> total = new AtomicReference<>(0D);
        productIdsToAmount.forEach((id, amount) -> {
            var price = products.get(id).getPrice();
            total.updateAndGet(currentTotal -> currentTotal + price * amount);
        });
        return total.get();
    }
}
