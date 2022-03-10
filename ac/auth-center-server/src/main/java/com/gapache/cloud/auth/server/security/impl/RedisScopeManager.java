package com.gapache.cloud.auth.server.security.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.gapache.cloud.auth.server.security.ScopeManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.gapache.cloud.auth.server.constant.RedisConstants.SCOPE_CACHE_PREFIX;

/**
 * @author HuSen
 * @since 2020/8/6 11:48 上午
 */
@Component
public class RedisScopeManager implements ScopeManager {

    private final StringRedisTemplate stringRedisTemplate;

    public RedisScopeManager(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public void save(String clientId, Long uid, Long timeout, Set<String> scopes) {
        String scopeCacheKey = SCOPE_CACHE_PREFIX + clientId + ":" + uid;
        ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
        String scopeStr = JSON.toJSONString(scopes);
        opsForValue.set(scopeCacheKey, scopeStr, timeout, TimeUnit.MILLISECONDS);
    }

    @Override
    public List<String> get(String clientId, Long uid) {
        String scopeCacheKey = SCOPE_CACHE_PREFIX + clientId + ":" + uid;
        ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
        String scopesStr = opsForValue.get(scopeCacheKey);
        if (StringUtils.isBlank(scopesStr)) {
            return null;
        }
        return JSONArray.parseArray(scopesStr, String.class);
    }
}
