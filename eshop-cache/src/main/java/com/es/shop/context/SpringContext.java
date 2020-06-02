package com.es.shop.context;


import org.springframework.context.ApplicationContext;

/**
 * @Description: spring上下文
 * @CreateTime: 2020-05-31
 * @Version:v1.0
 */
public class SpringContext {

    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static void setApplicationContext(ApplicationContext applicationContext) {
        SpringContext.applicationContext = applicationContext;
    }
}
