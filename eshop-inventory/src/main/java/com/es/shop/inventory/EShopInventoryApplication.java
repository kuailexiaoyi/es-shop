package com.es.shop.inventory;

import com.es.shop.inventory.listener.InitListener;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;

import java.util.EventListener;

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

    /**
     * @Desc: 随着系统启动而进行初始化
     * @Param
     * @Return org.springframework.boot.web.servlet.ServletListenerRegistrationBean
     * @Date: 2020/5/24
     */
    @Bean
    public ServletListenerRegistrationBean servletListenerRegistrationBean() {
        ServletListenerRegistrationBean<EventListener> servletListenerRegistrationBean = new ServletListenerRegistrationBean<>();
        servletListenerRegistrationBean.setListener(new InitListener());
        return servletListenerRegistrationBean;
    }
}
