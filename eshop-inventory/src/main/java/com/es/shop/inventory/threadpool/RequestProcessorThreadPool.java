package com.es.shop.inventory.threadpool;


import com.es.shop.inventory.request.Request;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @Description: 线程池初始化
 * @CreateTime: 2020-05-24
 * @Version:v1.0
 */
public class RequestProcessorThreadPool {

    /**
     * 创建线程池
     */
    private ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
            .setNameFormat("request-pool-%d").build();
    private ExecutorService threadPool = new ThreadPoolExecutor(5, 10,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

    /**
     * 创建队列
     */
    List<ArrayBlockingQueue<Request>> queues = new ArrayList<ArrayBlockingQueue<Request>>();


    public RequestProcessorThreadPool() {
        // 初始化内存队列
        RequestQueue requestQueue = RequestQueue.getInstance();
        for (int i = 0; i < 10; i++) {
            ArrayBlockingQueue<Request> queue = new ArrayBlockingQueue<Request>(100);
            requestQueue.addQueue(queue);
            threadPool.submit(new RequestProcessorThread(queue));
        }
    }


    /**
     * @Desc: 使用静态内部类的方式初始化，保证线程安全
     * @Date: 2020/5/24
     */
    private static class Singleton {
        private static RequestProcessorThreadPool requestProcessorThreadPool;

        static {
            requestProcessorThreadPool = new RequestProcessorThreadPool();
        }

        public static RequestProcessorThreadPool getInstance() {
            return requestProcessorThreadPool;
        }
    }

    /**
     * @Desc: 获取处理请求的线程池，JVM机制保证多线程并发安全，内部类的初始化，一定只会发生一次，不管多少线程并发去初始化。
     * @Param
     * @Return com.es.shop.inventory.threadpool.RequestProcessorThreadPool
     * @Date: 2020/5/24
     */
    public static RequestProcessorThreadPool getInstance() {
        return Singleton.getInstance();
    }

    public static void init() {
        getInstance();
    }
}
