package com.es.shop.mq.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.es.shop.entity.ProductInfo;
import com.es.shop.entity.ShopInfo;
import com.es.shop.service.CacheService;
import com.es.shop.zk.ZookeeperSession;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class KafkaConsumerController {

    private Logger logger = LoggerFactory.getLogger(KafkaConsumerController.class);

    @Autowired
    private CacheService cacheService;

    @KafkaListener(topics = {"eshop-cache-topic"})
    public void listen(ConsumerRecord<?, String> record, Acknowledgment acknowledgment) {
        // 将message消息串转换为JsonObject
        JSONObject jsonObject = JSONObject.parseObject(record.value());
        processMessage(jsonObject);
        acknowledgment.acknowledge();
    }

    /**
     * @Desc: 开始处理消息
     * @Param jsonObject
     * @Return void
     * @Date: 2020/6/1
     */
    private void processMessage(JSONObject jsonObject) {
        // 从消息中提取请求服务的标识
        String serviceId = jsonObject.getString("serviceId");
        // 提取商品Id和店铺Id
        Integer productId = jsonObject.getInteger("productId");

        Integer shopId = jsonObject.getInteger("shopId");

        // 根据服务类型的不同，处理消息
        if ("getProductInfo".equals(serviceId)) {
            processProductInfoMessage(productId);
        }
        if ("getShopInfo".equals(serviceId)) {
            processShopInfo(shopId);
        }
    }

    /**
     * @Desc: 处理店铺信息
     * @Param shopId
     * @Return void
     * @Date: 2020/5/31
     */
    private void processShopInfo(Integer shopId) {
        String shopInfoJson = "{\"id\":" + shopId + ", \"name\":\"李四店铺\", \"level\":皇冠5星, \"goodCommentRate\":\"5.0\"}";
        ShopInfo shopInfo = JSON.parseObject(shopInfoJson, ShopInfo.class);
        // 将数据存储到redis和ehcache中
        cacheService.saveShopInfo2LocalCache(shopId, shopInfo);
        cacheService.saveShopInfo2RedisCache(shopId, shopInfo);
    }

    /**
     * @Desc: 处理商品消息
     * @Param productId
     * @Return void
     * @Date: 2020/5/31
     */
    private void processProductInfoMessage(Integer productId) {
        // 调用商品信息服务的接口
        // 直接用注释模拟 getProductInfoFromLocalCache?productId=1
        // 商品信息服务，一般来说会直接查询数据库，获取productId=1的商品信息，然后返回回来
        // 模拟从数据库中获取到数据
        String productInfoJson = "{\"id\":" + productId + ", \"name\":\"iphone11 手机\", \"price\":5999, \"pictureList\":\"a.jpg, b.jpb\", \"specification\":\"iphone11 手机规格\", \"service\":\"iphone11 的售后服务\", \"color\":\"红色，黑色，白色\",\"size\":\"5.5寸\",\"modifiedTime\":\"2020-02-06 12:00:00\"}";
        ProductInfo productInfo = JSON.parseObject(productInfoJson, ProductInfo.class);
        logger.info("KafkaConsumerController.processProductInfoMessage process,productInfo : {}", JSON.toJSONString(productInfo));

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
                    logger.info("processProductInfoMessage process, 要更新的商品数据 比redis中的数据 的版本旧,放弃更新，productId:{},currentModifiedTime : {}, existsModifiedTime:{}", productId, currentModifiedDate, existModifiedDate);
                    return;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            logger.info("processProductInfoMessage process, 要更新的商品数据 比redis中的数据 的版本新，重新更新redis中的数据，productId:{}", productId);
        } else {
            logger.info("KafkaConsumerController.processProductInfoMessage product, exist product info is null");
        }

        try {
            // 休眠10s，
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 将数据存储到redis和ehcache中
        cacheService.saveProductInfo2RedisCache(productId, productInfo);
        logger.info("KafkaConsumerController.processProductInfoMessage process, 更新redis数据缓存成功，productId : {}", productId);
        cacheService.saveProductInfo2LocalCache(productId, productInfo);
        logger.info("KafkaConsumerController.processProductInfoMessage process, 更新数据到本地缓存成功，productId : {}", productId);

        // 业务逻辑处理完了之后，就释放分布式锁
        zookeeperSession.tryRelease(productId);
    }
}