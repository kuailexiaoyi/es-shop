package com.es.shop.inventory.request.impl;

import com.es.shop.inventory.entity.ProductInventoryDO;
import com.es.shop.inventory.request.Request;
import com.es.shop.inventory.service.productinventory.ProductInventoryService;

/**
 * @Description: 商品库存更新请求
 * @CreateTime: 2020-05-26
 * @Version:v1.0
 */
public class ProductInventoryDBUpdateRequest implements Request {

    private ProductInventoryDO productInventoryDO;

    private ProductInventoryService productInventoryService;

    @Override
    public void process() {

        // 删除redis中的缓存
        productInventoryService.removeProductInventoryCache(this.productInventoryDO.getProductId());

        // 更新商品库从
        productInventoryService.update(this.productInventoryDO);
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
