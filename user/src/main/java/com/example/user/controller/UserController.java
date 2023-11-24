package com.example.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.user.domain.User;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
public class UserController {

    @GetMapping("users/{userId}")
    public User getUser(@PathVariable String userId) {
        log.info("get User {} ", userId);
        return new User(userId, "name");
    }
}
