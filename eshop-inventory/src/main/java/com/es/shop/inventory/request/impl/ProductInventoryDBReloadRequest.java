package com.es.shop.inventory.request.impl;

import com.es.shop.inventory.entity.ProductInventoryDO;
import com.es.shop.inventory.request.Request;
import com.es.shop.inventory.service.productinventory.ProductInventoryService;

/**
 * @Description: 商品库存更新请求
 * @CreateTime: 2020-05-26
 * @Version:v1.0
 */
public class ProductInventoryDBReloadRequest implements Request {

    private int productId;

    private ProductInventoryService productInventoryService;

    public ProductInventoryDBReloadRequest() {
    }

    public ProductInventoryDBReloadRequest(int productId, ProductInventoryService productInventoryService) {
        this.productId = productId;
        this.productInventoryService = productInventoryService;
    }

    @Override
    public void process() {

        // 通过商品Id查询库存
        ProductInventoryDO productInventoryDO = productInventoryService.queryOne(this.productId);

        // 更新Redis中的商品库存緩存
        productInventoryService.refreshProductInventoryCache(productInventoryDO);
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
}
