package com.gapache.security.entity;

import com.gapache.vertx.redis.annotation.Id;
import com.gapache.vertx.redis.annotation.RedisEntity;
import lombok.Data;

/**
 * @author HuSen
 * @since 2021/3/25 12:44 下午
 */
@Data
@RedisEntity
public class RoleScopesEntity {

    @Id
    private Long roleId;

    private String scopes;
}
