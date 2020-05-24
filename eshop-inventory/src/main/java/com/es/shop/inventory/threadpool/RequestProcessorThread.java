package com.es.shop.inventory.threadpool;

import com.es.shop.inventory.request.Request;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;

/**
 * @Description: 执行请求的工作线程
 * @CreateTime: 2020-05-24
 * @Version:v1.0
 */
public class RequestProcessorThread implements Callable<Boolean> {

    /**
     * 自己监控的内存队列
     */
    private ArrayBlockingQueue<Request> queue;

    public RequestProcessorThread(ArrayBlockingQueue<Request> queue) {
        this.queue = queue;
    }

    @Override
    public Boolean call() throws Exception {
        // 线程变成后台线程，会不断的从跟自己绑定的queue中消费请求
        while (true) {
            break;
        }
        return true;
    }
}
