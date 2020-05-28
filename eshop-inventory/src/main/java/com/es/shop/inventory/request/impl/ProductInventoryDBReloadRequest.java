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
public class ProductInventoryDBReloadRequest implements Request {

    private Logger logger = LoggerFactory.getLogger(ProductInventoryDBReloadRequest.class);

    /**
     * 商品Id
     */
    private int productId;

    /**
     * 商品库存服务
     */
    private ProductInventoryService productInventoryService;

    /**
     * 是否强制刷新
     */
    private boolean forceRefresh;

    public ProductInventoryDBReloadRequest() {
    }

    public ProductInventoryDBReloadRequest(int productId, ProductInventoryService productInventoryService) {
        this(productId, productInventoryService, false);
    }

    public ProductInventoryDBReloadRequest(int productId, ProductInventoryService productInventoryService, boolean forceRefresh) {
        this.productId = productId;
        this.productInventoryService = productInventoryService;
        this.forceRefresh = forceRefresh;
    }

    @Override
    public void process() {
        logger.info("ProductInventoryDBReloadRequest.process , 开始执行库存读取操作，商品Id：{}", this.productId);
        // 通过商品Id查询库存
        ProductInventoryDO productInventoryDO = productInventoryService.queryOne(this.productId);
        logger.info("ProductInventoryDBReloadRequest.process , 从数据库获取商品数据成功，商品Id:{}, 商品库存：{}", productInventoryDO.getProductId(), productInventoryDO.getInventoryCnt());
        // 更新Redis中的商品库存緩存
        productInventoryService.refreshProductInventoryCache(productInventoryDO);
        logger.info("ProductInventoryDBReloadRequest.process process, 刷新Redis缓存成功，商品Id:{}, 商品库存：{}", productInventoryDO.getProductId(), productInventoryDO.getInventoryCnt());
    }

    @Override
    public int getProductId() {
        return this.productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public ProductInventoryService getProductInventoryService() {
        return productInventoryService;
    }

    public void setProductInventoryService(ProductInventoryService productInventoryService) {
        this.productInventoryService = productInventoryService;
    }

    @Override
    public boolean isForceRefresh() {
        return forceRefresh;
    }

    public void setForceRefresh(boolean forceRefresh) {
        this.forceRefresh = forceRefresh;
    }
}
