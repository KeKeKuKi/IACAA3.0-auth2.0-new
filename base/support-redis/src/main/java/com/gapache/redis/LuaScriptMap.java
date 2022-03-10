package com.gapache.redis;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.lang.NonNull;
import org.springframework.util.FileCopyUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author HuSen
 * create on 2019/12/5 11:37
 */
@Slf4j
public class LuaScriptMap {

    private static final Map<String, RedisScript<String>> REDIS_SCRIPT_MAP = new HashMap<>(12);

    protected void addLuaScript(String path) {
        if (StringUtils.isBlank(path)) {
            log.warn("path is blank!");
            return;
        }
        if (REDIS_SCRIPT_MAP.containsKey(path)) {
            log.warn("luaScrip:[{}] is existed!", path);
            return;
        }
        ClassPathResource resource = new ClassPathResource(path);
        try {
            byte[] bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
            RedisScript<String> redisScript = new DefaultRedisScript<>(new String(bytes, StandardCharsets.UTF_8), String.class);
            REDIS_SCRIPT_MAP.putIfAbsent(path, redisScript);
        } catch (Exception e) {
            log.error("[{}] luaScript is load fail:", path, e);
        }
    }

    @NonNull
    public RedisScript<String> getLuaScript(String path) {
        return REDIS_SCRIPT_MAP.get(path);
    }
}
