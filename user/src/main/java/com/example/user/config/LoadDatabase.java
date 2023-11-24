package com.example.user.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.user.domain.User;
import com.example.user.repo.UserRepo;

import lombok.extern.slf4j.Slf4j;



@Slf4j
@Configuration
class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(UserRepo userRepo) {
        return args -> {
            userRepo.save(new User("Antonio"));
            userRepo.save(new User("Alvaro"));

            userRepo.findAll().forEach(user -> {
                log.info("Preloaded " + user);
            });
        };
    }
}
