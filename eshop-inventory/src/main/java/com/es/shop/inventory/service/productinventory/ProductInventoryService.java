package com.es.shop.inventory.service.productinventory;

import com.es.shop.inventory.entity.ProductInventoryDO;

/**
 * @Description: 商品库存服务
 * @CreateTime: 2020-05-25
 * @Version:v1.0
 */
public interface ProductInventoryService {
    /**
     * @Desc: 查询商品库存
     * @Param productId
     * @Return com.es.shop.inventory.entity.ProductInventoryDO
     * @Date: 2020/5/25
     */
    ProductInventoryDO queryOne(int productId);

    /**
     * @Desc: 更新商品库存
     * @Param productInventoryDO
     * @Return int
     * @Date: 2020/5/25
     */
    int update(ProductInventoryDO productInventoryDO);

    /**
     * @Desc: 删除商品库存缓存
     * @Param productId
     * @Return boolean
     * @Date: 2020/5/26
     */
    boolean removeProductInventoryCache(Integer productId);

    /**
     * @Desc: 更新商品库存缓存
     * @Param productInventoryDO
     * @Return boolean
     * @Date: 2020/5/26
     */
    boolean refreshProductInventoryCache(ProductInventoryDO productInventoryDO);

    /**
     * @Desc: 从缓存获取商品库存数据
     * @Param productId
     * @Return com.es.shop.inventory.entity.ProductInventoryDO
     * @Date: 2020/5/26
     */
    ProductInventoryDO getProductInventoryFromCache(int productId);
}
