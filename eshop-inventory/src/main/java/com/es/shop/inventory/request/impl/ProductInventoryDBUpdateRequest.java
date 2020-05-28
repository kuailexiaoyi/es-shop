package com.es.shop.inventory.request.impl;

import com.es.shop.inventory.entity.ProductInventoryDO;
import com.es.shop.inventory.request.Request;
import com.es.shop.inventory.service.productinventory.ProductInventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description: 商品库存更新请求
 * @CreateTime: 2020-05-26
 * @Version:v1.0
 */
public class ProductInventoryDBUpdateRequest implements Request {

    private Logger logger = LoggerFactory.getLogger(ProductInventoryDBUpdateRequest.class);

    private ProductInventoryDO productInventoryDO;

    private ProductInventoryService productInventoryService;

    public ProductInventoryDBUpdateRequest() {
    }

    public ProductInventoryDBUpdateRequest(ProductInventoryDO productInventoryDO, ProductInventoryService productInventoryService) {
        this.productInventoryDO = productInventoryDO;
        this.productInventoryService = productInventoryService;
    }

    @Override
    public void process() {

        logger.info("ProductInventoryDBUpdateRequest.process, 数据库更新请求开始执行，商品Id:{},库存数量：{}", this.productInventoryDO.getProductId(), this.productInventoryDO.getInventoryCnt());
        // 删除redis中的缓存
        productInventoryService.removeProductInventoryCache(this.productInventoryDO.getProductId());

        try {
            // 模拟更新请求，删除缓存成功，更新缓存未完成，一个读请求过来 的场景
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        logger.info("ProductInventoryDBUpdateRequest.process , 已删除Redis中的库存缓存数据");
        // 更新商品库从
        productInventoryService.update(this.productInventoryDO);
        logger.info("ProductInventoryDBUpdateRequest.process , 已更新数据库中的库存数据，商品Id:{}, 库存数量：{}", this.productInventoryDO.getProductId(), this.productInventoryDO.getInventoryCnt());
    }

    @Override
    public int getProductId() {
        return this.productInventoryDO.getProductId();
    }

    public ProductInventoryDO getProductInventoryDO() {
        return productInventoryDO;
    }

    public void setProductInventoryDO(ProductInventoryDO productInventoryDO) {
        this.productInventoryDO = productInventoryDO;
    }

    public ProductInventoryService getProductInventoryService() {
        return productInventoryService;
    }

    public void setProductInventoryService(ProductInventoryService productInventoryService) {
        this.productInventoryService = productInventoryService;
    }

    @Override
    public boolean isForceRefresh() {
        return false;
    }
}
