package com.gapache.redis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author HuSen
 * @since 2021/1/28 2:40 下午
 */
@Data
@ConfigurationProperties(prefix = "com.gapache.redisson")
public class RedissonProperties {
    private String address;
    private Integer database;
    private String password;
}
