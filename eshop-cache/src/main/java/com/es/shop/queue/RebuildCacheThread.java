package com.es.shop.queue;

import com.es.shop.entity.ProductInfo;
import com.es.shop.service.CacheService;
import com.es.shop.zk.ZookeeperSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description: 处理重建缓存的队列中的数据
 * @CreateTime: 2020-06-02
 * @Version:v1.0
 */
public class RebuildCacheThread implements Runnable {
    private Logger logger = LoggerFactory.getLogger(RebuildCacheThread.class);

    private CacheService cacheService;

    public RebuildCacheThread(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @Override
    public void run() {
        RebuildCacheQueue rebuildCacheQueue = RebuildCacheQueue.getInstance();
        while (true) {
            // 从重建缓存的队列中获取数据
            ProductInfo productInfo = rebuildCacheQueue.takeProductInfo();
            int productId = productInfo.getId();

            // 新增逻辑，当将数据刷新到redis缓存的时候，应该先获取锁
            ZookeeperSession zookeeperSession = ZookeeperSession.getInstance();

            // 尝试获取锁，如果获取锁成功，就会继续往下执行，如果获取锁失败，就会hang住在这里，知道成功获取锁。
            zookeeperSession.tryAcquire(productId);

            // 获取锁之后，需要比较数据版本，如果当前更新的版本比数据库中的数据要新，就进行更新否则就释放锁。
            ProductInfo existProductInfo = cacheService.getProductInfoFromRedisCache(productId);
            if (existProductInfo != null) {
                String modifiedTime = existProductInfo.getModifiedTime();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
                try {
                    Date existModifiedDate = simpleDateFormat.parse(modifiedTime);
                    Date currentModifiedDate = simpleDateFormat.parse(productInfo.getModifiedTime());
                    if (currentModifiedDate.before(existModifiedDate)) {
                        logger.info("RebuildCacheThread process, 要更新的商品数据 比redis中的数据 的版本旧,放弃更新，productId:{},currentModifiedTime : {}, existsModifiedTime:{}", productId, currentModifiedDate, existModifiedDate);
                        return;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                logger.info("RebuildCacheThread process, 要更新的商品数据 比redis中的数据 的版本新，重新更新redis中的数据，productId:{}", productId);
            } else {
                logger.info("RebuildCacheThread.processProductInfoMessage product, exist product info is null");
            }

            // 将数据存储到redis和ehcache中
            cacheService.saveProductInfo2RedisCache(productId, productInfo);
            logger.info("RebuildCacheThread.processProductInfoMessage process, 更新redis数据缓存成功，productId : {}", productId);
            cacheService.saveProductInfo2LocalCache(productId, productInfo);
            logger.info("RebuildCacheThread.processProductInfoMessage process, 更新数据到本地缓存成功，productId : {}", productId);

            // 业务逻辑处理完了之后，就释放分布式锁
            zookeeperSession.tryRelease(productId);
        }
    }

    public CacheService getCacheService() {
        return cacheService;
    }

    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }
}
