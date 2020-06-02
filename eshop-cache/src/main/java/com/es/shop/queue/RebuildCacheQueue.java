package com.es.shop.queue;

import com.es.shop.entity.ProductInfo;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @Description:
 * @CreateTime: 2020-06-02
 * @Version:v1.0
 */
public class RebuildCacheQueue {

    private static ArrayBlockingQueue<ProductInfo> arrayBlockingQueue = new ArrayBlockingQueue<ProductInfo>(1000);


    public RebuildCacheQueue() {

    }

    /**
     * @Desc: 往队列中添加请求
     * @Param productInfo
     * @Return void
     * @Date: 2020/6/2
     */
    public void addProductInfo(ProductInfo productInfo) {
        try {
            arrayBlockingQueue.put(productInfo);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Desc: 从队列中获取请求
     * @Param
     * @Return com.es.shop.entity.ProductInfo
     * @Date: 2020/6/2
     */
    public ProductInfo takeProductInfo() {
        try {
            return arrayBlockingQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @Desc: 获取重建缓存的队列
     * @Param
     * @Return com.es.shop.zk.ZookeeperSession
     * @Date: 2020/6/2
     */
    public static RebuildCacheQueue getInstance() {
        return Singleton.getSingleton();
    }

    static class Singleton {
        private static RebuildCacheQueue rebuildCacheQueue;

        static {
            rebuildCacheQueue = new RebuildCacheQueue();
        }

        public static RebuildCacheQueue getSingleton() {
            return rebuildCacheQueue;
        }
    }
}
