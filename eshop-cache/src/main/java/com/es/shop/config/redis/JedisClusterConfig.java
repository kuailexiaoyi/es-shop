package com.es.shop.config.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class JedisClusterConfig {

    @Bean
    public JedisCluster getJedisCluster() {
        Set<HostAndPort> nodes = new HashSet<HostAndPort>();
        nodes.add(new HostAndPort("192.168.0.102", 7001));
        nodes.add(new HostAndPort("192.168.0.102", 7002));
        nodes.add(new HostAndPort("192.168.0.103", 7003));
        nodes.add(new HostAndPort("192.168.0.103", 7004));
        nodes.add(new HostAndPort("192.168.0.104", 7005));
        nodes.add(new HostAndPort("192.168.0.104", 7006));
        JedisCluster jedisCluster = new JedisCluster(nodes);
        return jedisCluster;
    }
}