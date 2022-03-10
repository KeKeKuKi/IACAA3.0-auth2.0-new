package com.gapache.vertx.redis;

import com.gapache.vertx.redis.annotation.Id;
import com.gapache.vertx.redis.annotation.RedisEntity;
import lombok.Data;

/**
 * @author HuSen
 * @since 2021/3/8 1:18 下午
 */
@Data
@RedisEntity
public class Cat {

    @Id
    private String id;

    private String name;

    private Integer age;
}
