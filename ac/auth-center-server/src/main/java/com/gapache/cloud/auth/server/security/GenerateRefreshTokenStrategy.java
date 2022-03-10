package com.gapache.cloud.auth.server.security;

import java.util.Map;

/**
 * 生成refresh token的策略接口
 *
 * @author HuSen
 * @since 2020/8/4 6:26 下午
 */
public interface GenerateRefreshTokenStrategy {

    /**
     * 生成refresh token
     *
     * @param params 生成refresh token的参数
     * @return refreshToken
     */
    String generate(Map<String, Object> params);
}
