package com.es.shop.inventory;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Description:
 * @CreateTime: 2020-05-24
 * @Version:v1.0
 */
@SpringBootApplication
@MapperScans(value = {@MapperScan("com.es.shop.inventory.mapper")})
public class EShopInventoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(EShopInventoryApplication.class, args);
    }
}
