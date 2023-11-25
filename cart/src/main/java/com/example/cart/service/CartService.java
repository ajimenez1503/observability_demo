package com.example.cart.service;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.cart.client.ProductClient;
import com.example.cart.client.UserClient;
import com.example.cart.domain.Product;
import com.example.cart.domain.User;

import io.opentelemetry.instrumentation.annotations.SpanAttribute;
import io.opentelemetry.instrumentation.annotations.WithSpan;
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

    public Map<Long, Product> getProductsV2(Collection<Long> productIds) {
        try {
            var result = productClient.getProducts(productIds.stream().toList());
            if (result.getStatusCode().is2xxSuccessful()) {
                return result.getBody()
                             .stream()
                             .collect(Collectors.toMap(Product::getId, Function.identity()));
            }
            else {
                log.error("ProductIds {} were not found.", productIds);
            }
        }
        catch (Exception e) {
            log.error("Could not find products with ids '{}'.", productIds, e);
        }
        return Collections.emptyMap();
    }

    public Map<Long, Product> getProductsV3(Collection<Long> productIds) {
        try {
            var result = productClient.getProducts(productIds.stream().toList());
            if (result.getStatusCode().is2xxSuccessful()) {
                var resultProducts = result.getBody();
                if (productIds.size() == resultProducts.size()) {
                    return resultProducts.stream()
                                         .collect(Collectors.toMap(Product::getId,
                                                                   Function.identity()));
                }
            }
            log.error("ProductIds {} were not found.", productIds);
        }
        catch (Exception e) {
            log.error("Could not find products with ids '{}'.", productIds, e);
        }
        return Collections.emptyMap();
    }

    @WithSpan
    public double getTotal(@SpanAttribute Map<Long, Product> products,
                           @SpanAttribute Map<Long, Long> productIdsToAmount)
    {
        AtomicReference<Double> total = new AtomicReference<>(0D);
        productIdsToAmount.forEach((id, amount) -> {
            var price = products.get(id).getPrice();
            total.updateAndGet(currentTotal -> Util.round(currentTotal + price * amount));
        });
        return total.get();
    }
}
