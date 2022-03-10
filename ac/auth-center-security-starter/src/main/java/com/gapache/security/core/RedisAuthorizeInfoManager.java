package com.gapache.security.core;

import com.gapache.security.entity.IdTokenEntity;
import com.gapache.security.interfaces.AsyncAuthorizeInfoManager;
import com.gapache.security.interfaces.AuthorizeInfoManager;
import com.gapache.security.model.CustomerInfo;
import com.gapache.vertx.redis.support.SimpleRedisRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author HuSen
 * @since 2020/8/4 6:19 下午
 */
@Slf4j
public class RedisAuthorizeInfoManager implements AuthorizeInfoManager {

    private final AsyncAuthorizeInfoManager asyncAuthorizeInfoManager;
    private final SimpleRedisRepository simpleRedisRepository;

    public RedisAuthorizeInfoManager(AsyncAuthorizeInfoManager asyncAuthorizeInfoManager, SimpleRedisRepository simpleRedisRepository) {
        this.asyncAuthorizeInfoManager = asyncAuthorizeInfoManager;
        this.simpleRedisRepository = simpleRedisRepository;
    }

    @Override
    public void save(Long userId, String token, Long timeout, CustomerInfo customerInfo, Collection<String> scopes) {
        AtomicBoolean wait = new AtomicBoolean(false);
        asyncAuthorizeInfoManager.save(token, TimeUnit.SECONDS.convert(timeout, TimeUnit.MILLISECONDS), customerInfo, scopes)
                .onComplete(as -> {
                    wait.compareAndSet(false, true);
                    if (!as.succeeded()) {
                        log.error("save fail.", as.cause());
                    }
                });
        IdTokenEntity idTokenEntity = new IdTokenEntity();
        idTokenEntity.setUserId(userId);
        idTokenEntity.setToken(token);

        simpleRedisRepository.save(idTokenEntity, TimeUnit.SECONDS.convert(timeout, TimeUnit.MILLISECONDS))
                .onComplete(res -> {
                    if (res.succeeded()) {
                        log.info(">>>>>> save idTokenEntity success");
                    } else {
                        log.error(">>>>>> save idTokenEntity fail.", res.cause());
                    }
                });

        while (!wait.get()) {
            try {
                TimeUnit.MILLISECONDS.sleep(1);
            } catch (InterruptedException ignored) {
            }
        }
    }

    @Override
    public void delete(String token) {
        AtomicBoolean wait = new AtomicBoolean(false);
        asyncAuthorizeInfoManager.delete(token)
                .onComplete(as -> {
                    wait.compareAndSet(false, true);
                    if (!as.succeeded()) {
                        log.error("delete fail.", as.cause());
                    }
                });

        while (!wait.get()) {
            try {
                TimeUnit.MILLISECONDS.sleep(1);
            } catch (InterruptedException ignored) {
            }
        }
    }

    @Override
    public String get(String token) {
        // TODO 暂时没用到，不用实现
        return null;
    }
}
