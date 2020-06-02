package com.es.shop.service;

import com.es.shop.entity.ProductInfo;
import com.es.shop.entity.ShopInfo;

/**
 * @Description:
 * @CreateTime: 2020-05-28
 * @Version:v1.0
 */
public interface CacheService {

    /**
     * @Desc: 从ecache中获取数据
     * @Param id
     * @Return com.es.shop.entity.ProductInfo
     * @Date: 2020/5/28
     */
    ProductInfo getProductInfoFromLocalCache(int id);

    /**
     * @Desc: 往escache
     * @Param productInfo
     * @Return com.es.shop.entity.ProductInfo
     * @Date: 2020/5/28
     */
    ProductInfo saveProductInfo2LocalCache(int id, ProductInfo productInfo);

    /**
     * @Desc: 从redis中获取数据
     * @Param id
     * @Return com.es.shop.entity.ProductInfo
     * @Date: 2020/5/28
     */
    ProductInfo getProductInfoFromRedisCache(int id);

    /**
     * @Desc: 往redis中写入数据
     * @Param productInfo
     * @Return com.es.shop.entity.ProductInfo
     * @Date: 2020/5/28
     */
    void saveProductInfo2RedisCache(int id, ProductInfo productInfo);


    /**
     * @Desc: 从ecache中获取数据
     * @Param id
     * @Return com.es.shop.entity.ShopInfo
     * @Date: 2020/5/28
     */
    ShopInfo getShopInfoFromLocalCache(int id);

    /**
     * @Desc: 往escache
     * @Param ShopInfo
     * @Return com.es.shop.entity.ShopInfo
     * @Date: 2020/5/28
     */
    ShopInfo saveShopInfo2LocalCache(int id, ShopInfo ShopInfo);

    /**
     * @Desc: 从redis中获取数据
     * @Param id
     * @Return com.es.shop.entity.ShopInfo
     * @Date: 2020/5/28
     */
    ShopInfo getShopInfoFromRedisCache(int id);

    /**
     * @Desc: 往redis中写入数据
     * @Param ShopInfo
     * @Return com.es.shop.entity.ShopInfo
     * @Date: 2020/5/28
     */
    void saveShopInfo2RedisCache(int id, ShopInfo ShopInfo);

}
