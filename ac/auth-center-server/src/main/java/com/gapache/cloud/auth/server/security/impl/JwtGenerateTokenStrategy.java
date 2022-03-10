package com.gapache.cloud.auth.server.security.impl;

import com.gapache.security.interfaces.GenerateTokenStrategy;
import com.gapache.security.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;

import java.security.PrivateKey;
import java.util.Map;

/**
 * jwt token 生成策略
 *
 * @author HuSen
 * @since 2020/8/4 6:01 下午
 */
@Slf4j
public class JwtGenerateTokenStrategy implements GenerateTokenStrategy {

    private static final String CONTENT = "content";
    private static final String TIMEOUT = "timeout";

    private final PrivateKey privateKey;

    public JwtGenerateTokenStrategy(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    @Override
    public String generate(Map<String, Object> params) {
        log.info("generate params:{}", params);
        if (!params.containsKey(CONTENT) || !params.containsKey(TIMEOUT)) {
            return null;
        }

        String content = (String) params.get(CONTENT);
        Long timeout = (Long) params.get(TIMEOUT);

        return JwtUtils.generateToken(content, privateKey, timeout);
    }
}
