package com.example.cart.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.cart.client.UserClient;
import com.example.cart.domain.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;



@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {
    private final UserClient userClient;

    public Optional<User> getUser(Long userId) {
        try {
            var result = userClient.getUser(userId);
            if (result.getStatusCode().is2xxSuccessful()) {
                return Optional.ofNullable(result.getBody());
            } else {
                log.error("Could not find user with id '{}'. Error code {}", userId, result.getStatusCode());
            }

        } catch (Exception e) {
            log.error("Could not find user with id '{}'.", userId, e);
        }
        return Optional.empty();
    }
}
