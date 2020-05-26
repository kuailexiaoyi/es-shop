package com.es.shop.inventory.service.productinventory.impl;

import com.es.shop.inventory.entity.ProductInventoryDO;
import com.es.shop.inventory.mapper.productinventory.ProductInventoryMapper;
import com.es.shop.inventory.service.productinventory.ProductInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description: 商品库存服务实现类
 * @CreateTime: 2020-05-25
 * @Version:v1.0
 */
@Service
public class ProductInventoryServiceImpl implements ProductInventoryService {

    @Autowired
    private ProductInventoryMapper productInventoryMapper;


    @Override
    public ProductInventoryDO queryOne(int productId) {
        return productInventoryMapper.queryOne(productId);
    }

    @Override
    public int update(ProductInventoryDO productInventoryDO) {
        return productInventoryMapper.update(productInventoryDO);
    }
}
