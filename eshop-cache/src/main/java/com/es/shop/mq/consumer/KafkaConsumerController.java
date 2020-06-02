package com.es.shop.mq.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.es.shop.entity.ProductInfo;
import com.es.shop.entity.ShopInfo;
import com.es.shop.service.CacheService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

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
        String productInfoJson = "{\"id\":" + productId + ", \"name\":\"iphone11 手机\", \"price\":5999, \"pictureList\":\"a.jpg, b.jpb\", \"specification\":\"iphone11 手机规格\", \"service\":\"iphone11 的售后服务\", \"color\":\"红色，黑色，白色\",\"size\":\"5.5寸\"}";
        ProductInfo productInfo = JSON.parseObject(productInfoJson, ProductInfo.class);
        logger.info("KafkaConsumerController.processProductInfoMessage process,productInfo : {}", JSON.toJSONString(productInfo));
        // 将数据存储到redis和ehcache中
        cacheService.saveProductInfo2RedisCache(productId, productInfo);
        logger.info("KafkaConsumerController.processProductInfoMessage process, 更新redis数据缓存成功，productId : {}", productId);
        cacheService.saveProductInfo2LocalCache(productId, productInfo);
        logger.info("KafkaConsumerController.processProductInfoMessage process, 更新数据到本地缓存成功，productId : {}", productId);
    }
}