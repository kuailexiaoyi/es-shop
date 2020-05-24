package com.es.shop.inventory.listener;

import com.es.shop.inventory.threadpool.RequestProcessorThreadPool;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @Description: 系统初始化，跟着web应用的启动就进行初始化
 * @CreateTime: 2020-05-24
 * @Version:v1.0
 */
public class InitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        // 初始化工作线程池和工作队列
        RequestProcessorThreadPool.init();
        System.out.println("线程池初始化=====================================");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
