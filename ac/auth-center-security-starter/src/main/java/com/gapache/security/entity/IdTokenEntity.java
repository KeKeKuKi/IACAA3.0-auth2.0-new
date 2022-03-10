package com.gapache.security.entity;

import com.gapache.vertx.redis.annotation.Id;
import com.gapache.vertx.redis.annotation.RedisEntity;
import lombok.Data;

/**
 * @author HuSen
 * @since 2021/3/25 10:50 上午
 */
@Data
@RedisEntity("tb_id_token")
public class IdTokenEntity {

    @Id
    private Long userId;

    private String token;
}
