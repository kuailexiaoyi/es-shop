package com.es.shop.service.impl;

import com.es.shop.service.EhcacheService;
import com.es.shop.entity.ProductInfo;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @CreateTime: 2020-05-28
 * @Version:v1.0
 */
@Service
public class EhcacheServiceImpl implements EhcacheService {

    @Override
    @Cacheable(value = "local", key = "'product_info_' + #ide")
    public ProductInfo getProductInfo(int id) {
        return null;
    }

    @Override
    @CachePut(value = "local", key = "'product_info_' + #productInfo.getId()")
    public ProductInfo saveProductInfo(int id, ProductInfo productInfo) {
        return productInfo;
    }
}
