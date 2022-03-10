package com.gapache.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.Arrays;
import java.util.List;

/**
 * @author HuSen
 * create on 2019/12/5 11:32
 */
@Slf4j
public class RedisLuaExecutor {

    private final StringRedisTemplate stringRedisTemplate;
    private final LuaScriptMap luaScriptMap;

    public RedisLuaExecutor(StringRedisTemplate stringRedisTemplate, LuaScriptMap luaScriptMap) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.luaScriptMap = luaScriptMap;
    }

    public String execute(LuaScript script, List<String> keys, Object... args) {
        return execute(script, RedisSerializer.string(), keys, args);
    }

    public String execute(LuaScript script, RedisSerializer<?> argsSerializer, List<String> keys, Object... args) {
        try {
            if (log.isDebugEnabled()) {
                log.debug("redis 执行 lua 脚本:{}, keys:{}, args:{}", script.path(), keys, Arrays.toString(args));
            }
            return stringRedisTemplate.execute(luaScriptMap.getLuaScript(script.path()), argsSerializer, RedisSerializer.string(), keys, args);
        } catch (Exception e) {
            log.error("Execute Lua Script error.", e);
            return "9999";
        }
    }
}
