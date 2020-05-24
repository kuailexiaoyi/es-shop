package com.es.shop.inventory.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisTemplateConfig {

    @Autowired
    private RedisClusterConfigProperties redisClusterConfigProperties;

    @Autowired
    private JedisConfigProperties jedisConfigProperties;

    @Bean
    public RedisClusterConfiguration getRedisClusterConfiguration() {
        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(redisClusterConfigProperties.getNodes());

        redisClusterConfiguration.setMaxRedirects(redisClusterConfigProperties.getMaxRedirects());

        return redisClusterConfiguration;
    }

    @Bean
    public JedisPoolConfig getJedisPoolConfig() {
        // 设置jedis客户端配置
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(jedisConfigProperties.getMaxIdle());
        jedisPoolConfig.setMinIdle(jedisConfigProperties.getMinIdle());
        jedisPoolConfig.setMaxWaitMillis(jedisConfigProperties.getMaxWait());
        jedisPoolConfig.setMaxTotal(jedisConfigProperties.getMaxActive());
        return jedisPoolConfig;
    }

    // 1.项目启动时此方法先被注册成bean被spring管理,
    // 如果没有这个bean，
    // 则redis可视化工具中的中文内容（key或者value）都会以二进制存储，不易检查。
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(redisConnectionFactory);

        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // key采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // hash的key也采用String的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        // value序列化方式采用jackson
        template.setValueSerializer(jackson2JsonRedisSerializer);
        // hash的value序列化方式采用jackson
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }
}