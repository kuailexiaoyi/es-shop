package com.es.shop.inventory.entity;

/**
 * @Description: 商品库存DO类
 * @CreateTime: 2020-05-25
 * @Version:v1.0
 */
public class ProductInventoryDO {

    /**
     * 商品Id
     */
    private Integer productId;

    /**
     * 库存数量
     */
    private Integer inventoryCnt;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getInventoryCnt() {
        return inventoryCnt;
    }

    public void setInventoryCnt(Integer inventoryCnt) {
        this.inventoryCnt = inventoryCnt;
    }
}
