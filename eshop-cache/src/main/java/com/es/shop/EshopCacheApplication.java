package com.es.shop;

import com.es.shop.listener.InitListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import java.util.EventListener;

/**
 * Hello world!
 */
@SpringBootApplication
@EnableCaching
public class EshopCacheApplication {
    public static void main(String[] args) {
        SpringApplication.run(EshopCacheApplication.class);
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
