package com.es.shop.inventory.request;

/**
 * @Description:
 * @CreateTime: 2020-05-24
 * @Version:v1.0
 */
public interface Request {

    /**
     * @Desc: 处理请求
     * @Param
     * @Return void
     * @Date: 2020/5/26
     */
    void process();

    /**
     * @Desc: 获取商品Id
     * @Param
     * @Return int
     * @Date: 2020/5/26
     */
    int getProductId();
}
