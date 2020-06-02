package com.es.shop.controller;

import com.alibaba.fastjson.JSON;
import com.es.shop.base.Result;
import com.es.shop.entity.ProductInfo;
import com.es.shop.entity.ShopInfo;
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
        logger.info("CacheController.getProductInfoFromLocalCache start,从ehcache中获取数据,id : {}", productId);
        ProductInfo productInfo = cacheService.getProductInfoFromLocalCache(productId);
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
