package com.gapache.cloud.auth.server.security;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.gapache.cloud.auth.server.constant.GrantType;
import com.gapache.cloud.auth.server.model.AuthorizeTokenDTO;
import com.gapache.cloud.auth.server.model.ClientDetailsImpl;
import com.gapache.cloud.auth.server.model.RefreshTokenDTO;
import com.gapache.commons.model.ThrowUtils;
import com.gapache.security.model.SecurityError;
import com.gapache.security.model.TokenInfoDTO;
import com.gapache.security.model.impl.CertificationImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.gapache.cloud.auth.server.constant.RedisConstants.REFRESH_TOKEN_PREFIX;
import static com.gapache.cloud.auth.server.constant.RedisConstants.SCOPE_CACHE_PREFIX;

/**
 * @author HuSen
 * @since 2020/8/5 10:04 上午
 */
public abstract class BaseGenerateTokenLogic {

    private StringRedisTemplate stringRedisTemplate;
    private GenerateRefreshTokenStrategy generateRefreshTokenStrategy;

    public BaseGenerateTokenLogic() {
    }

    protected BaseGenerateTokenLogic(StringRedisTemplate stringRedisTemplate, GenerateRefreshTokenStrategy generateRefreshTokenStrategy) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.generateRefreshTokenStrategy = generateRefreshTokenStrategy;
    }

    /**
     * 执行生成token的逻辑处理
     *
     * @param authorizeTokenDTO 生成Token所需要的数据封装
     * @param clientDetails     ClientDetailsImpl
     * @return Token信息的数据封装
     */
    protected abstract TokenInfoDTO doLogic(AuthorizeTokenDTO authorizeTokenDTO, ClientDetailsImpl clientDetails);

    /**
     * 获取ClientDetailsImpl
     *
     * @param clientId clientId
     * @return ClientDetailsImpl
     */
    protected abstract ClientDetailsImpl getClientDetails(String clientId);

    private void preCheck(GrantType grantType, ClientDetailsImpl clientDetails) {
        ThrowUtils.throwIfTrue(clientDetails == null || !clientDetails.getGrantTypes().contains(grantType), SecurityError.CLIENT_ERROR);
    }

    public TokenInfoDTO execute(AuthorizeTokenDTO authorizeTokenDTO) {
        ClientDetailsImpl clientDetails = getClientDetails(authorizeTokenDTO.getClientId());
        preCheck(authorizeTokenDTO.getGrantType(), clientDetails);
        return doLogic(authorizeTokenDTO, clientDetails);
    }

    protected Map<String, Object> buildGenerateTokenParams(Long timeout, String username, Long id, String clientId, boolean sign) {
        Map<String, Object> generateTokenParams = new HashMap<>(4);
        CertificationImpl certification = new CertificationImpl();
        certification.setName(username);
        certification.setId(id);
        certification.setClientId(clientId);
        certification.setSign(sign);
        String content = JSON.toJSONString(certification);
        generateTokenParams.put("content", content);
        generateTokenParams.put("timeout", timeout);
        return generateTokenParams;
    }

    protected List<String> loadAuthorizedScope(String clientId, Long userId, ValueOperations<String, String> opsForValue) {
        String scopeCacheStr = opsForValue.get(SCOPE_CACHE_PREFIX + clientId + ":" + userId);
        List<String> scopes = new ArrayList<>();
        if (StringUtils.isNotBlank(scopeCacheStr)) {
            scopes = JSONArray.parseArray(scopeCacheStr, String.class);
        }
        return scopes;
    }

    protected String generateRefreshToken(String oldRefreshToken, Long userId, String token, Long refreshTokenTimeout) {
        ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
        String newRefreshToken = generateRefreshTokenStrategy.generate(null);
        RefreshTokenDTO refreshTokenDTO = new RefreshTokenDTO();
        refreshTokenDTO.setUserId(userId);
        refreshTokenDTO.setAccessToken(token);
        // 删除旧的refresh token
        if (StringUtils.isNotBlank(oldRefreshToken)) {
            stringRedisTemplate.delete(oldRefreshToken);
        }
        // 保存新的refresh token
        opsForValue.setIfAbsent(REFRESH_TOKEN_PREFIX + newRefreshToken, JSON.toJSONString(refreshTokenDTO), refreshTokenTimeout, TimeUnit.MILLISECONDS);
        return newRefreshToken;
    }
}
