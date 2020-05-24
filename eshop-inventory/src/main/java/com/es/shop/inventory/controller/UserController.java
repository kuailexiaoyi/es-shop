package com.es.shop.inventory.controller;

import com.es.shop.inventory.entity.UserDO;
import com.es.shop.inventory.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Description:
 * @CreateTime: 2020-05-24
 * @Version:v1.0
 */
@Controller
@RequestMapping
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/queryAll")
    @ResponseBody
    public List<UserDO> queryAll() {
        return userService.queryAll();
    }

    @RequestMapping("/queryOne")
    @ResponseBody
    public UserDO queryOne(@RequestParam("id") int id) {
        return userService.queryOne(id);
    }
}
