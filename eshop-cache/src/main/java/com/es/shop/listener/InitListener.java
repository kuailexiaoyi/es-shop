package com.es.shop.listener;

import com.es.shop.context.SpringContext;
import com.es.shop.queue.RebuildCacheThread;
import com.es.shop.service.CacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @Description: 系统初始化监听器
 * @CreateTime: 2020-05-31
 * @Version:v1.0
 */
public class InitListener implements ServletContextListener {
    private Logger logger = LoggerFactory.getLogger(InitListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        SpringContext.setApplicationContext(applicationContext);
        logger.info("InitListener.contextInitialized start, 初始化kafka消费者，sce : {}", sce);
        // new Thread(new KafkaConsumer("cache-message")).start();
        // 初始化重建缓存线程
        new Thread(new RebuildCacheThread((CacheService) applicationContext.getBean("cacheService"))).start();

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
