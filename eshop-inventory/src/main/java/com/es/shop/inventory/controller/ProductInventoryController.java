package com.es.shop.inventory.controller;

import com.alibaba.fastjson.JSON;
import com.es.shop.inventory.base.Result;
import com.es.shop.inventory.entity.ProductInventoryDO;
import com.es.shop.inventory.request.Request;
import com.es.shop.inventory.request.RequestAsyncProcessService;
import com.es.shop.inventory.request.impl.ProductInventoryDBReloadRequest;
import com.es.shop.inventory.service.productinventory.ProductInventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description:
 * @CreateTime: 2020-05-24
 * @Version:v1.0
 */
@Controller
@RequestMapping
public class ProductInventoryController {

    private Logger logger = LoggerFactory.getLogger(ProductInventoryController.class);

    @Autowired
    private ProductInventoryService productInventoryService;

    @Autowired
    private RequestAsyncProcessService requestAsyncProcessService;

    @RequestMapping("/queryProductInventory")
    @ResponseBody
    public Result queryOne(int productId) {

        ProductInventoryDO productInventoryDO;
        // 新增数据读请求
        try {
            Request request = new ProductInventoryDBReloadRequest(productId, productInventoryService);
            requestAsyncProcessService.process(request);

            // 将请求扔给service异步服务处理之后，就需要while(true)一会儿，在这里hang一会，
            // 尝试等待前面的商品库存更新操作，同时刷新新的缓存，将最新的数据刷新到缓存中。
            long startTime = System.currentTimeMillis();
            long waitTime = 0, endTime = 0;
            while (true) {
                // 如果查Redis 耗费了200ms，就break掉
                if (waitTime > 200) {
                    break;
                }
                // 尝试去redis获取一次商品库存的缓存数据
                productInventoryDO = productInventoryService.getProductInventoryFromCache(productId);

                // 如果从缓存中获取到数据就直接返回
                if (productInventoryDO != null) {
                    return Result.success(productInventoryDO);
                }

                //    如果没有从缓存中获取到数据，就等待20ms
                Thread.sleep(20);
                endTime = System.currentTimeMillis();
                waitTime = endTime - startTime;
            }

            // redis没有获取到数据，尝试从数据库获取数据
            productInventoryDO = productInventoryService.queryOne(productId);
            logger.info("ProductInventoryController.queryOne, 从数据库获取数据,productId : {}", productId, JSON.toJSONString(productInventoryDO));
            // 刷新缓存
            if (productInventoryDO != null) {
                productInventoryService.refreshProductInventoryCache(productInventoryDO);
                logger.info("ProductInventoryController.queryOne,刷新Redis缓存,productId : {}", productId);
            }

        } catch (Exception e) {
            logger.error("ProductInventoryController.queryOne error,productId : {}", productId, e);
            return Result.fail();
        }
    }

    @RequestMapping("/updateProductInventory")
    @ResponseBody
    public int udpate(@RequestParam("productId") int productId, @RequestParam("inventoryCnt") int inventoryCnt) {
        ProductInventoryDO productInventoryDO = new ProductInventoryDO();
        productInventoryDO.setInventoryCnt(inventoryCnt);
        productInventoryDO.setProductId(productId);
        return productInventoryService.update(productInventoryDO);
    }
}
