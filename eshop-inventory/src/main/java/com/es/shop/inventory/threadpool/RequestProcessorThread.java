package com.es.shop.inventory.threadpool;

import com.es.shop.inventory.request.Request;
import com.es.shop.inventory.request.impl.ProductInventoryDBUpdateRequest;

import java.util.Map;
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
        try {
            while (true) {
                Request request = this.queue.take();

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
                            flagMap.put(request.getProductId(), false);
                        }

                        // 如果为true,表示该读请求前面还有一个写请求，标记为设置为false
                        if (flag != null && flag) {
                            flagMap.put(request.getProductId(), false);
                        }

                        // 如果为false,表示该读请求前面已经有一个数据更新请求和一个缓存刷新请求了，对于这种请求
                        // 就直接过滤掉,即读请求去重
                        if (flag != null && !flag) {
                            return true;
                        }
                    }

                }
                // 假如说，执行完一个读请求之后，假设数据刷新到redis了，
                // 但是后面可能redis的数据因为内存满了，就自动清理掉了
                // 如果说数据从redis中被自动清理掉了以后，然后后面又一个读请求过来了，
                // 此时发现标志为false，就不会去执行这个刷新操作了。
                // 处理请求
                request.process();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
