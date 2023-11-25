package com.example.cart.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.cart.domain.User;



@FeignClient(url = "http://localhost:8081", name = "user-client")
public interface UserClient {
    @GetMapping(path = "/users/{userId}")
    ResponseEntity<User> getUser(@PathVariable("userId") Long id);
}
