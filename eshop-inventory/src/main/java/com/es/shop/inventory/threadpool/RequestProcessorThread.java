package com.es.shop.inventory.threadpool;

import com.es.shop.inventory.request.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;

/**
 * @Description: 执行请求的工作线程
 * @CreateTime: 2020-05-24
 * @Version:v1.0
 */
public class RequestProcessorThread implements Callable<Boolean> {

    private Logger logger = LoggerFactory.getLogger(RequestProcessorThread.class);

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
        try {
            while (true) {
                Request request = this.queue.take();
                // 执行这个process操作
                logger.info("RequestProcessorThread.call process, 工作线程开始处理请求，商品Id：{}", request.getProductId());
                request.process();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
