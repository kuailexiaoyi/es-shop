package com.es.shop.inventory.threadpool;


import com.es.shop.inventory.request.Request;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @Description: 请求内存队列
 * @CreateTime: 2020-05-24
 * @Version:v1.0
 */
public class RequestQueue {
    /**
     * 初始化内存队列
     */
    List<ArrayBlockingQueue<Request>> queues = new ArrayList<ArrayBlockingQueue<Request>>();

    public RequestQueue() {
    }


    /**
     * @Desc: 使用静态内部类的方式初始化，保证线程安全
     * @Date: 2020/5/24
     */
    private static class Singleton {
        private static RequestQueue requestQueue;

        static {
            requestQueue = new RequestQueue();
        }

        public static RequestQueue getInstance() {
            return requestQueue;
        }
    }

    /**
     * @Desc: 获取处理请求的线程池，JVM机制保证多线程并发安全，内部类的初始化，一定只会发生一次，不管多少线程并发去初始化。
     * @Param
     * @Return com.es.shop.inventory.threadpool.RequestProcessorThreadPool
     * @Date: 2020/5/24
     */
    public static RequestQueue getInstance() {
        return Singleton.getInstance();
    }


    public void addQueue(ArrayBlockingQueue<Request> arrayBlockingQueue) {
        this.queues.add(arrayBlockingQueue);
    }
}
