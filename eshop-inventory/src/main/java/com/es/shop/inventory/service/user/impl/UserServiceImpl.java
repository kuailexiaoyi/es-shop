package com.es.shop.inventory.service.user.impl;

import com.es.shop.inventory.entity.UserDO;
import com.es.shop.inventory.mapper.UserMapper;
import com.es.shop.inventory.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @CreateTime: 2020-05-24
 * @Version:v1.0
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<UserDO> queryAll() {
        return userMapper.queryAll();
    }

    @Override
    public UserDO queryOne(int id) {
        return userMapper.queryOne(id);
    }
}
