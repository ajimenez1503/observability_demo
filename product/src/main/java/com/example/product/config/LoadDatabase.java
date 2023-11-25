package com.example.product.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.product.domain.Category;
import com.example.product.domain.Product;
import com.example.product.repo.ProductRepo;

import lombok.extern.slf4j.Slf4j;



@Slf4j
@Configuration
class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(ProductRepo repo) {
        return args -> {
            repo.save(new Product("Water", 0.5, "Bottle of water", Category.FOOD));
            repo.save(new Product("Pizza", 10.5, "Piza margarita", Category.FOOD));
            repo.save(new Product("Beer", 1.8, "Bottle of beer", Category.FOOD));

            repo.findAll().forEach(product -> {
                log.info("Preloaded " + product);
            });
        };
    }
}
