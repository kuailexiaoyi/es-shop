package com.es.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Hello world!
 */
@SpringBootApplication
@EnableCaching
public class EshopCacheApplication {
    public static void main(String[] args) {
        SpringApplication.run(EshopCacheApplication.class);
    }
}
