package com.gapache.cloud.auth.server.security.impl;

import com.alibaba.fastjson.JSON;
import com.gapache.cloud.auth.server.model.CodeCacheInfoDTO;
import com.gapache.cloud.auth.server.security.CodeStrategy;
import com.gapache.security.model.CustomerInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.gapache.cloud.auth.server.constant.RedisConstants.CODE_CACHE_PREFIX;

/**
 * @author HuSen
 * @since 2020/8/6 10:55 上午
 */
@Component
public class UUIDCodeStrategy implements CodeStrategy {

    private static final int EXPIRE_IN = 5;

    private final StringRedisTemplate stringRedisTemplate;

    public UUIDCodeStrategy(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public String generate(Long uid, String name, CustomerInfo customerInfo) {
        String code = UUID.randomUUID().toString().replace("-", "");
        CodeCacheInfoDTO codeCacheInfoDTO = new CodeCacheInfoDTO();
        codeCacheInfoDTO.setUserId(uid);
        codeCacheInfoDTO.setUsername(name);
        codeCacheInfoDTO.setCustomerInfo(customerInfo);
        // code有效时长为5分钟
        ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
        opsForValue.set(CODE_CACHE_PREFIX + code, JSON.toJSONString(codeCacheInfoDTO), EXPIRE_IN, TimeUnit.MINUTES);
        return code;
    }

    @Override
    public CodeCacheInfoDTO get(String code) {
        ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
        String codeInfoStr = opsForValue.get(CODE_CACHE_PREFIX + code);
        if (StringUtils.isBlank(codeInfoStr)) {
            return null;
        }
        return JSON.parseObject(codeInfoStr, CodeCacheInfoDTO.class);
    }

    @Override
    public void delete(String code) {
        stringRedisTemplate.delete(CODE_CACHE_PREFIX + code);
    }
}
