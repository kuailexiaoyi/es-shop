package com.es.shop.inventory.request.impl;

import com.alibaba.fastjson.JSON;
import com.es.shop.inventory.request.Request;
import com.es.shop.inventory.request.RequestAsyncProcessService;
import com.es.shop.inventory.threadpool.RequestQueue;
import org.springframework.stereotype.Service;

import java.util.Map;
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
    public boolean process(Request request) {
        // 从Request中获取productId
        int productId = request.getProductId();

        // 如果不是强制刷新缓存的操作，就需要进行去重，如果是强制刷新缓存操作，就直接进行数据的刷新操作
        if (!request.isForceRefresh()) {
            // ArrayBlcokingQueue 在队列满了的时候，或者说是空的时候，就会被阻塞住
            RequestQueue requestQueue = RequestQueue.getInstance();
            Map<Integer, Boolean> flagMap = requestQueue.getFlagMap();

            // 如果是写请求，就将该productId对应的读写标记为设置为true。
            if (request instanceof ProductInventoryDBUpdateRequest) {
                flagMap.put(request.getProductId(), true);
            } else {
                Boolean flag = flagMap.get(request.getProductId());
                // 如果为null,表示该商品库存没有被更新过，标记为设置为false
                if (flag == null) {
                    logger.info("RequestAsyncProcessServiceImpl.process process, 首次处理读请求,并将标志位设置为false，商品Id : {}", request.getProductId());
                    flagMap.put(request.getProductId(), false);
                }

                // 如果为true,表示该读请求前面还有一个写请求，标记为设置为false
                if (flag != null && flag) {
                    logger.info("RequestAsyncProcessServiceImpl.process process, 处理读请求时，发现队列中已经存在写请求，将标志位设置为false，商品Id : {}", request.getProductId());
                    flagMap.put(request.getProductId(), false);
                }

                // 如果为false,表示该读请求前面已经有一个数据更新请求和一个缓存刷新请求了，对于这种请求
                // 就直接过滤掉,即读请求去重
                if (flag != null && !flag) {
                    logger.info("RequestAsyncProcessServiceImpl.process process, 处理读请求时，发现队列中已经存在读请求，过滤该读请求，商品Id : {}", request.getProductId());
                    return true;
                }
            }

        }
        // 假如说，执行完一个读请求之后，假设数据刷新到redis了，
        // 但是后面可能redis的数据因为内存满了，就自动清理掉了
        // 如果说数据从redis中被自动清理掉了以后，然后后面又一个读请求过来了，
        // 此时发现标志为false，就不会去执行这个刷新操作了。
        // 处理请求
        // 将请求路由到指定队列中
        ArrayBlockingQueue<Request> routingQueue = getRoutingQueue(productId);
        try {
            routingQueue.put(request);
        } catch (InterruptedException e) {
            logger.error("RequestAsyncProcessServiceImpl.process error,request : {}", JSON.toJSONString(request), e);
        }
        return true;
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
        logger.info("RequestAsyncProcessServiceImpl.getRoutingQueue process, 将请求路由到内存队列，productId : {}，队列索引：{}", productId, hash % instance.getSize());
        return result;
    }
}
