package com.gapache.security.entity;

import com.gapache.vertx.redis.annotation.Id;
import com.gapache.vertx.redis.annotation.RedisEntity;
import lombok.Data;

/**
 * @author HuSen
 * @since 2021/3/18 2:22 下午
 */
@Data
@RedisEntity("tb_authorize_info")
public class AuthorizeInfoEntity {

    @Id
    private String id;
    private String scopes;
    private String customerInfo;
}
