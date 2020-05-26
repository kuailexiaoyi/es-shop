package com.es.shop.inventory.request.impl;

import com.alibaba.fastjson.JSON;
import com.es.shop.inventory.request.Request;
import com.es.shop.inventory.request.RequestAsyncProcessService;
import com.es.shop.inventory.threadpool.RequestQueue;
import org.springframework.stereotype.Service;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @Description: 请求异步处理服务：将请求通过productId路由到响应队列中
 * @CreateTime: 2020-05-26
 * @Version:v1.0
 */
@Service
public class RequestAsyncProcessServiceImpl implements RequestAsyncProcessService {

    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(RequestAsyncProcessServiceImpl.class);

    @Override
    public void process(Request request) {
        // 从Request中获取productId
        int productId = request.getProductId();
        // 将请求路由到指定队列中
        ArrayBlockingQueue<Request> routingQueue = getRoutingQueue(productId);
        try {
            routingQueue.put(request);
        } catch (InterruptedException e) {
            logger.error("RequestAsyncProcessServiceImpl.process error,request : {}", JSON.toJSONString(request), e);
        }
    }

    /**
     * @Desc: 根据porductId获取队列
     * @Param productId
     * @Return java.util.concurrent.ArrayBlockingQueue<com.es.shop.inventory.request.Request>
     * @Date: 2020/5/26
     */
    private ArrayBlockingQueue<Request> getRoutingQueue(int productId) {
        RequestQueue instance = RequestQueue.getInstance();
        // 先获取productId的hash值
        String key = String.valueOf(productId);
        int h;
        int hash = (h = key.hashCode()) ^ (h >>> 16);

        // 对hash值取模，将hash值路由到指定队列中去，比如队列内存为8，
        //  使用hash值对队列数量取模，结果肯定是在0~7之间
        // 所以同一个productId会都会固定路由到同一个内存队列中去
        ArrayBlockingQueue<Request> result = instance.getQueue(hash % instance.getSize());
        return result;
    }
}
