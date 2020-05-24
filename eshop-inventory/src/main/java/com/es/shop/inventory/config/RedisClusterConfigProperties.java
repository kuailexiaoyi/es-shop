package com.es.shop.inventory.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Desc: redis集群版属性配置类
 * @Date: 2020/5/24
 */
@Component
@ConfigurationProperties(prefix = "spring.redis.cluster")
public class RedisClusterConfigProperties {

    private List<String> nodes;

    private Integer maxRedirects = 3;

    public List<String> getNodes() {
        return nodes;
    }

    public void setNodes(List<String> nodes) {
        this.nodes = nodes;
    }

    public Integer getMaxRedirects() {
        return maxRedirects;
    }

    public void setMaxRedirects(Integer maxRedirects) {
        this.maxRedirects = maxRedirects;
    }
}

