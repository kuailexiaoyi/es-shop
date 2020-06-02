package com.es.shop.controller;

import com.alibaba.fastjson.JSON;
import com.es.shop.base.Result;
import com.es.shop.entity.ProductInfo;
import com.es.shop.entity.ShopInfo;
import com.es.shop.queue.RebuildCacheQueue;
import com.es.shop.service.CacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description:
 * @CreateTime: 2020-05-28
 * @Version:v1.0
 */
@Controller
public class CacheController {
    private Logger logger = LoggerFactory.getLogger(CacheController.class);

    @Autowired
    private CacheService cacheService;

    @RequestMapping("/getProductInfo")
    @ResponseBody
    public ProductInfo getProductInfo(@RequestParam("productId") int productId) {

        ProductInfo productInfo = cacheService.getProductInfoFromRedisCache(productId);
        if (productInfo == null) {
            logger.info("CacheController.getProductInfo process , 从Redis中获取数据失败，productId : {}", productId);
        }

        productInfo = cacheService.getProductInfoFromLocalCache(productId);

        if (productInfo == null) {
            logger.info("CacheController.getProductInfoFromLocalCache process,从ehcache中获取数据失败,id : {}", productId);
        }
        // 如果redis中和ehcache中都没有获取到数据，就直接数据库中获取数据
        if (productInfo == null) {
            String productInfoJson = "{\"id\":" + productId + ", \"name\":\"iphone11 手机\", \"price\":5999, \"pictureList\":\"a.jpg, b.jpb\", \"specification\":\"iphone11 手机规格\", \"service\":\"iphone11 的售后服务\", \"color\":\"红色，黑色，白色\",\"size\":\"5.5寸\",\"modifiedTime\":\"2020-02-06 12:01:00\"}";
            productInfo = JSON.parseObject(productInfoJson, ProductInfo.class);
            // 将请求推送到一个重建缓存的内存队列中
            RebuildCacheQueue.getInstance().addProductInfo(productInfo);
        }
        return productInfo;
    }

    @RequestMapping("/saveProductInfo/{id}")
    @ResponseBody
    public Result saveProductInfo(@PathVariable("id") int id, ProductInfo productInfo) {
        logger.info("CacheController.saveProductInfo2LocalCache start,保存数据到ehcache，productInfo : {}", JSON.toJSONString(productInfo));
        cacheService.saveProductInfo2LocalCache(id, productInfo);
        return Result.success();
    }

    @RequestMapping("/getShopInfo")
    @ResponseBody
    public ShopInfo getShopInfo(@RequestParam("shopId") int shopId) {
        logger.info("CacheController.getShopInfo start,从ehcache中获取数据,id : {}", shopId);
        return cacheService.getShopInfoFromLocalCache(shopId);
    }
}
