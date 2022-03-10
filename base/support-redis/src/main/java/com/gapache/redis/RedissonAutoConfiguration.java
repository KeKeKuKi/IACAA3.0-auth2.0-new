package com.gapache.redis;

import com.gapache.redis.lock.DistributedLock;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

/**
 * @author HuSen
 * @since 2021/1/28 2:33 下午
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(RedissonProperties.class)
public class RedissonAutoConfiguration {

    @ConditionalOnMissingBean
    @Bean
    @ConditionalOnProperty(prefix = "com.gapache.redisson", value = "address")
    public RedissonClient redissonClient(RedissonProperties redissonProperties) {
        Assert.hasText(redissonProperties.getAddress(), "address is must require");

        Config config = new Config();
        SingleServerConfig singleServerConfig = config.useSingleServer().setAddress(redissonProperties.getAddress());

        if (redissonProperties.getDatabase() != null) {
            singleServerConfig.setDatabase(redissonProperties.getDatabase());
        } else {
            singleServerConfig.setDatabase(0);
        }

        if (StringUtils.isNotBlank(redissonProperties.getPassword())) {
            singleServerConfig.setPassword(redissonProperties.getPassword());
        }

        RedissonClient redissonClient = Redisson.create(config);
        DistributedLock.setRedissonClient(redissonClient);

        log.info("init redissonClient and DistributedLock success");

        return redissonClient;
    }
}
