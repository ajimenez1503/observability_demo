package com.example.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.user.domain.User;
import com.example.user.repo.UserRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;



@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserRepo userRepo;

    @GetMapping("users/{userId}")
    public ResponseEntity<User> getUser(@PathVariable Long userId) {
        var user = userRepo.findById(userId);
        if (user.isPresent()) {
            log.info("get User by id {} ", userId);
            return ResponseEntity.ok(user.get());
        }
        else {
            log.error("get User by id {} not found ", userId);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("users")
    public ResponseEntity<User> createUser(@RequestBody String name) {
        log.info("create user with name {} ", name);
        var newUser = new User(name);
        var user = userRepo.save(newUser);
        return ResponseEntity.ok(user);
    }
}
