package com.es.shop.inventory.service.productinventory.impl;

import com.es.shop.inventory.entity.ProductInventoryDO;
import com.es.shop.inventory.mapper.productinventory.ProductInventoryMapper;
import com.es.shop.inventory.service.productinventory.ProductInventoryService;
import com.es.shop.inventory.service.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @Description: 商品库存服务实现类
 * @CreateTime: 2020-05-25
 * @Version:v1.0
 */
@Service
public class ProductInventoryServiceImpl implements ProductInventoryService {

    @Autowired
    private ProductInventoryMapper productInventoryMapper;

    @Autowired
    private RedisService redisService;


    @Override
    public ProductInventoryDO queryOne(int productId) {
        return productInventoryMapper.queryOne(productId);
    }

    @Override
    public int update(ProductInventoryDO productInventoryDO) {
        return productInventoryMapper.update(productInventoryDO);
    }

    @Override
    public boolean removeProductInventoryCache(Integer productId) {
        redisService.delete(getRedisKey(productId));
        return true;
    }

    @Override
    public boolean refreshProductInventoryCache(ProductInventoryDO productInventoryDO) {
        redisService.setValue(getRedisKey(productInventoryDO.getProductId()), String.valueOf(productInventoryDO.getInventoryCnt()));
        return true;
    }

    /**
     * @Desc: 获取库存在Redis中的key
     * @Param productId
     * @Return java.lang.String
     * @Date: 2020/5/26
     */
    public String getRedisKey(int productId) {
        return "product:inventory:" + productId;
    }

    @Override
    public ProductInventoryDO getProductInventoryFromCache(int productId) {
        String inventoryCntStr = redisService.getValue(getRedisKey(productId));
        if (StringUtils.isEmpty(inventoryCntStr)) {
            return null;
        }
        ProductInventoryDO productInventoryDO = new ProductInventoryDO();
        productInventoryDO.setProductId(productId);
        productInventoryDO.setInventoryCnt(Integer.valueOf(inventoryCntStr));
        return productInventoryDO;
    }
}
