package com.es.shop.service.impl;

import com.alibaba.fastjson.JSON;
import com.es.shop.entity.ShopInfo;
import com.es.shop.service.CacheService;
import com.es.shop.entity.ProductInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

/**
 * @Description: 缓存服务
 * @CreateTime: 2020-05-28
 * @Version:v1.0
 */
@Service("cacheService")
public class CacheServiceImpl implements CacheService {

    private Logger logger = LoggerFactory.getLogger(CacheServiceImpl.class);

    @Autowired
    private JedisCluster jedisCluster;

    @Override
    @Cacheable(value = "local", key = "'product_info_' + #id")
    public ProductInfo getProductInfoFromLocalCache(int id) {
        logger.info("CacheServiceImpl.getProductInfoFromLocalCache start, 未从LocaCache中获取商品信息，id : {}", id);
        ProductInfo productInfoFromRedisCache = getProductInfoFromRedisCache(id);
        logger.info("CacheServiceImpl.getProductInfoFromLocalCache process, 从Redis中获取店铺信息，id : {}", id);
        return productInfoFromRedisCache;
    }

    @Override
    @CachePut(value = "local", key = "'product_info_' + #productInfo.id")
    public ProductInfo saveProductInfo2LocalCache(int id, ProductInfo productInfo) {
        return productInfo;
    }

    @Override
    public ProductInfo getProductInfoFromRedisCache(int id) {
        String productInfoJson = jedisCluster.get("product_info_" + id);
        ProductInfo productInfo = JSON.parseObject(productInfoJson, ProductInfo.class);
        return productInfo;
    }

    @Override
    public void saveProductInfo2RedisCache(int id, ProductInfo productInfo) {
        jedisCluster.set("product_info_" + productInfo.getId(), JSON.toJSONString(productInfo));
        return;
    }

    @Override
    @Cacheable(value = "local", key = "'shop_info_' + #id")
    public ShopInfo getShopInfoFromLocalCache(int id) {
        logger.info("CacheServiceImpl.getShopInfoFromLocalCache start, 未从本地缓存中获取消息，尝试从redis中获取消息，id : {}", id);
        ShopInfo shopInfoFromRedisCache = getShopInfoFromRedisCache(id);
        logger.info("CacheServiceImpl.getShopInfoFromLocalCache process, 从redis中获取数据成功，id : {}", id);
        return shopInfoFromRedisCache;
    }

    @Override
    @CachePut(value = "local", key = "'shop_info_' + #shopInfo.id")
    public ShopInfo saveShopInfo2LocalCache(int id, ShopInfo shopInfo) {
        return shopInfo;
    }

    @Override
    public ShopInfo getShopInfoFromRedisCache(int id) {
        String shopInfoJson = jedisCluster.get("shop_info_" + id);
        ShopInfo shopInfo = JSON.parseObject(shopInfoJson, ShopInfo.class);
        return shopInfo;
    }

    @Override
    public void saveShopInfo2RedisCache(int id, ShopInfo ShopInfo) {
        jedisCluster.set("shop_info_" + id, JSON.toJSONString(ShopInfo));
        return;
    }
}
