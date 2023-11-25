package com.example.product.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.product.domain.Product;
import com.example.product.repo.ProductRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;



@Slf4j
@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductRepo productRepo;

    @GetMapping("products/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable Long productId) {
        var product = productRepo.findById(productId);
        if (product.isPresent()) {
            log.info("get product by id {} ", productId);
            return ResponseEntity.ok(product.get());
        }
        else {
            log.error("get product by id {} not found.", productId);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("products/search")
    public ResponseEntity<List<Product>> getProducts(@RequestBody List<Long> productIds) {
        List<Product> products = new ArrayList<>();
        productIds.forEach(id -> {
            var product = productRepo.findById(id);
            if (product.isPresent()) {
                products.add(product.get());
            }
            else {
                log.error("Product with id {} was not found.", id);
            }
        });
        log.info("Requested products by id {}. And got product by id {} ", productIds,
                 products.stream().map(Product::getId).toList());
        return ResponseEntity.ok(products);
    }
}
