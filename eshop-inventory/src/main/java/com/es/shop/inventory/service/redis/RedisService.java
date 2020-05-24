package com.es.shop.inventory.service.redis;

/**
 * @Description:
 * @CreateTime: 2020-05-24
 * @Version:v1.0
 */
public interface RedisService {
    /**
     * 功能描述: 设置key-value
     *
     * @param key
     * @param value
     * @Author: zr
     * @Date: 2019/11/20
     * @return: void
     */
    void setValue(String key, String value);

    /*
     *功能描述 ：根据key获取value
     *
     * @author zr
     * @date 2019/11/20
     * @param key
     * @return java.lang.String
     */
    String getValue(String key);

    /**
     * 功能描述: 删除缓存
     *
     * @param key
     * @return: void
     * @author: zr
     * @date: 2019/11/30
     */
    Boolean delete(String key);
}
