package com.es.shop.inventory.controller;

import com.alibaba.fastjson.JSON;
import com.es.shop.inventory.base.Result;
import com.es.shop.inventory.entity.ProductInventoryDO;
import com.es.shop.inventory.request.Request;
import com.es.shop.inventory.request.RequestAsyncProcessService;
import com.es.shop.inventory.request.impl.ProductInventoryDBReloadRequest;
import com.es.shop.inventory.request.impl.ProductInventoryDBUpdateRequest;
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
            logger.info("ProductInventoryController.queryOne start, 接收到库存查询的请求，productId : {}", productId);
            requestAsyncProcessService.process(request);

            // 将请求扔给service异步服务处理之后，就需要while(true)一会儿，在这里hang一会，
            // 尝试等待前面的商品库存更新操作，同时刷新新的缓存，将最新的数据刷新到缓存中。
            long startTime = System.currentTimeMillis();
            long waitTime = 0, endTime = 0;
            while (true) {
                // 如果查Redis 耗费了200ms，就break掉
                if (waitTime > 20000) {
                    break;
                }
                // 尝试去redis获取一次商品库存的缓存数据
                productInventoryDO = productInventoryService.getProductInventoryFromCache(productId);

                // 如果从缓存中获取到数据就直接返回
                if (productInventoryDO != null) {
                    logger.info("ProductInventoryController.queryOne proces, 200毫秒内，读请求操作从缓存中获取数据，productId : {},inventoryCnt:{}", productId, productInventoryDO.getInventoryCnt());
                    return Result.success(productInventoryDO);
                }

                //    如果没有从缓存中获取到数据，就等待20ms
                Thread.sleep(20);
                endTime = System.currentTimeMillis();
                waitTime = endTime - startTime;
            }

            // redis没有获取到数据，尝试从数据库获取数据
            productInventoryDO = productInventoryService.queryOne(productId);
            logger.info("ProductInventoryController.queryOne, 200毫秒内，读操作没有从缓存中获取数据，开始从数据库获取数据,productId : {}", productId, JSON.toJSONString(productInventoryDO));
            // 刷新缓存
            if (productInventoryDO != null) {
                // 将数据刷新一下
                // 这个过程，实际上是一个读操作的过程，也必须放到队列中去，但是还是有数据不一致的问题。
                Request readReqest = new ProductInventoryDBReloadRequest(productId, productInventoryService, true);
                requestAsyncProcessService.process(readReqest);
                logger.info("ProductInventoryController.queryOne,强制刷新Redis缓存,productId : {}", productId);
                // 代码运行到这里，只有三种情况
                // 1、上一次请求也是读请求，但是数据被刷新到了Redis，但是Redis通过LRU算法将数据给清理掉了，标志为还是false。
                // 此时下一个读请求过来，在缓存中还是拿不到数据，再放一个读Request进队列，让数据区刷新换一下。
                // 2、可能在200ms内，未从redis中读取到数据，没有等待到他执行。所以就直接查一次库，然后给队列中添加一个读Request。
                // 3、数据库中本身就没有
            }
            return Result.success(productInventoryDO);
        } catch (Exception e) {
            logger.error("ProductInventoryController.queryOne error,productId : {}", productId, e);
            return Result.fail();
        }
    }

    @RequestMapping("/updateProductInventory")
    @ResponseBody
    public Result udpate(@RequestParam("productId") int productId, @RequestParam("inventoryCnt") int inventoryCnt) {
        ProductInventoryDO productInventoryDO = new ProductInventoryDO();
        productInventoryDO.setInventoryCnt(inventoryCnt);
        productInventoryDO.setProductId(productId);
        Request request = new ProductInventoryDBUpdateRequest(productInventoryDO, productInventoryService);
        logger.info("ProductInventoryController.udpate process, 接收到库存更新的请求，productId : {}, inventoryCnt : {}", productId, inventoryCnt);
        requestAsyncProcessService.process(request);
        return Result.success(1);
    }
}
