package com.es.shop.controller;

import com.alibaba.fastjson.JSON;
import com.es.shop.entity.ProductInfo;
import com.es.shop.service.EhcacheService;
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
@RequestMapping("/v1")
public class EhcacheController {
    private Logger logger = LoggerFactory.getLogger(EhcacheController.class);

    @Autowired
    private EhcacheService ehcacheService;

    @RequestMapping("/getProductInfo")
    @ResponseBody
    public ProductInfo getProductInfo(@RequestParam("id") int id) {
        logger.info("EhcacheController.getProductInfo start,从ehcache中获取数据,id : {}", id);
        return ehcacheService.getProductInfo(id);
    }

    @RequestMapping("/saveProductInfo/{id}")
    @ResponseBody
    public ProductInfo saveProductInfo(@PathVariable("id") int id, ProductInfo productInfo) {
        logger.info("EhcacheController.saveProductInfo start,保存数据到ehcache，productInfo : {}", JSON.toJSONString(productInfo));
        return ehcacheService.saveProductInfo(id,productInfo);
    }
}
