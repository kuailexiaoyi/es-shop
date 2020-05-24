package com.es.shop.inventory.config;/**
 * @Description:
 * @CreateTime: 2020-05-24
 * @Version:v1.0
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @Description: RedisClusterConfig
 * @Date: 2020-05-24
 * @Version:v1.0
 */

@Configuration
public class RedisClusterConfig {

    @Autowired
    private JedisConfigProperties jedisConfigProperties;

    @Autowired
    private RedisClusterConfigProperties redisClusterConfigProperties;

    /**
     * @Desc: redis连接工厂，注意：使用集群版redis工厂，注意屏蔽单机版redis工厂
     * @Param jedisPoolConfig
     * @Return org.springframework.data.redis.connection.RedisConnectionFactory
     * @Date: 2020/5/24
     */
    @Bean
    public RedisConnectionFactory connectionFactory(JedisPoolConfig jedisPoolConfig) {
        return new JedisConnectionFactory(
                new RedisClusterConfiguration(redisClusterConfigProperties.getNodes()), jedisPoolConfig);
    }

}
