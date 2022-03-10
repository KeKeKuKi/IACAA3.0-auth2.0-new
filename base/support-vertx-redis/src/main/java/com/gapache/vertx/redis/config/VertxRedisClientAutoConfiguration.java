package com.gapache.vertx.redis.config;

import com.gapache.vertx.core.VertxManager;
import com.gapache.vertx.redis.support.SimpleRedisRepository;
import io.vertx.redis.client.Command;
import io.vertx.redis.client.Redis;
import io.vertx.redis.client.RedisOptions;
import io.vertx.redis.client.Request;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author HuSen
 * @since 2021/3/5 5:30 下午
 */
@Slf4j
@Configuration
public class VertxRedisClientAutoConfiguration {

    @Bean
    public Redis redisClient(ApplicationContext context) {
        Environment environment = context.getEnvironment();
        String host = environment.getProperty("spring.redis.host");
        String port = environment.getProperty("spring.redis.port");
        String password = environment.getProperty("spring.redis.password");

        RedisOptions redisOptions = new RedisOptions()
                .setConnectionString("redis://" + (StringUtils.isBlank(host) ? "localhost" : host) + ":" + (StringUtils.isBlank(port) ? "6379" : port));
        if (StringUtils.isNotBlank(password)) {
            redisOptions.setPassword(password);
        }

        Redis redis = Redis.createClient(
                VertxManager.checkNewStandalone(),
                redisOptions
                        .setMaxPoolSize(8)
                        .setMaxWaitingHandlers(32));

        redis.send(Request.cmd(Command.PING))
                .onSuccess(res -> log.info(">>>>>> 连接到redis{} {} 成功", host, port));

        return redis;
    }

    @Bean
    public SimpleRedisRepository simpleRedisRepository(Redis redis) {
        return new SimpleRedisRepository(redis);
    }
}
