package com.example.cart.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.cart.domain.Product;



@FeignClient(url = "http://localhost:8082", name = "product-client")
public interface ProductClient {
    @GetMapping(path = "/products/{productId}")
    ResponseEntity<Product> getProduct(@PathVariable("productId") Long id);

    @PostMapping(path = "/products/search")
    ResponseEntity<List<Product>> getProducts(@RequestBody List<Long> ids);
}
