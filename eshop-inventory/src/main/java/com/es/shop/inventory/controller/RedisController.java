package com.es.shop.inventory.controller;

import com.es.shop.inventory.service.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description:
 * @Auther: zrblog
 * @CreateTime: 2019-11-19 07:37
 * @Version:v1.0
 */
@Controller
@RequestMapping("/redis")
public class RedisController {

    @Autowired
    private RedisService redisService;

    @RequestMapping("/set")
    @ResponseBody
    public String setValue(@RequestParam("key") String key, @RequestParam("value") String value) {
        redisService.setValue(key, value);
        return "success";
    }

    @RequestMapping("/get")
    @ResponseBody
    public String getValue(@RequestParam("key") String key) {
        String result = redisService.getValue(key);
        return result;
    }

}
