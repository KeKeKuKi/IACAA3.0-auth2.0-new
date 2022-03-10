package com.gapache.redis;

import org.apache.commons.collections4.MapUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Map;

/**
 * @author HuSen
 * create on 2020/1/15 11:51
 */
@ConditionalOnBean(annotation = EnableRedis.class)
public class RedisAutoConfiguration {

    private final ApplicationContext applicationContext;

    public RedisAutoConfiguration(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    @Primary
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate redisTemplate = new StringRedisTemplate(redisConnectionFactory);
        Map<String, Object> enableRedisOnClassBean = applicationContext.getBeansWithAnnotation(EnableRedis.class);
        if (MapUtils.isNotEmpty(enableRedisOnClassBean)) {
            EnableRedis enableRedis = AnnotationUtils.findAnnotation(enableRedisOnClassBean.values().iterator().next().getClass(), EnableRedis.class);
            if (enableRedis != null) {
                redisTemplate.setEnableTransactionSupport(enableRedis.enableTransactionSupport());
            }
        }
        return redisTemplate;
    }
}
