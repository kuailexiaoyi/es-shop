package com.es.shop.service;

import com.es.shop.entity.ProductInfo;

/**
 * @Description:
 * @CreateTime: 2020-05-28
 * @Version:v1.0
 */
public interface EhcacheService {

    /**
     * @Desc: 从ecache中获取数据
     * @Param id
     * @Return com.es.shop.entity.ProductInfo
     * @Date: 2020/5/28
     */
    ProductInfo getProductInfo(int id);

    /**
     * @Desc: 往escache
     * @Param productInfo
     * @Return com.es.shop.entity.ProductInfo
     * @Date: 2020/5/28
     */
    ProductInfo saveProductInfo(int id, ProductInfo productInfo);
}
