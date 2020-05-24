package com.es.shop.inventory.mapper;

import com.es.shop.inventory.entity.UserDO;

import java.util.List;

/**
 * @Description:
 * @CreateTime: 2020-05-24
 * @Version:v1.0
 */
public interface UserMapper {

    /**
     * @Desc: 查询所有记录
     * @Param
     * @Return java.util.List<com.es.shop.inventory.entity.UserDO>
     * @Date: 2020/5/24
     */
    List<UserDO> queryAll();

    /**
     * @Desc: 查询指定记录
     * @Param id
     * @Return com.es.shop.inventory.entity.UserDO
     * @Date: 2020/5/24
     */
    UserDO queryOne(int id);
}
