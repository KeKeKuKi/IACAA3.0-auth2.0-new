package com.gapache.cloud.auth.server.security.impl;

import com.gapache.cloud.auth.server.constant.GrantType;
import com.gapache.cloud.auth.server.model.AuthorizeTokenDTO;
import com.gapache.cloud.auth.server.model.ClientDetailsImpl;
import com.gapache.cloud.auth.server.model.CodeCacheInfoDTO;
import com.gapache.security.interfaces.AuthorizeInfoManager;
import com.gapache.cloud.auth.server.security.BaseGenerateTokenLogic;
import com.gapache.cloud.auth.server.security.CodeStrategy;
import com.gapache.cloud.auth.server.security.GenerateRefreshTokenStrategy;
import com.gapache.cloud.auth.server.service.ClientService;
import com.gapache.commons.model.ThrowUtils;
import com.gapache.security.interfaces.GenerateTokenStrategy;
import com.gapache.security.model.SecurityError;
import com.gapache.security.model.TokenInfoDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author HuSen
 * @since 2020/8/5 10:42 上午
 */
@Component("authorization_code")
public class AuthorizationCodeGenerateTokenLogic extends BaseGenerateTokenLogic {

    private final ClientService clientService;
    private final PasswordEncoder passwordEncoder;
    private final StringRedisTemplate stringRedisTemplate;
    private final GenerateTokenStrategy generateTokenStrategy;
    private final AuthorizeInfoManager authorizeInfoManager;
    private final CodeStrategy codeStrategy;

    public AuthorizationCodeGenerateTokenLogic(ClientService clientService, PasswordEncoder passwordEncoder, StringRedisTemplate stringRedisTemplate, GenerateTokenStrategy generateTokenStrategy, AuthorizeInfoManager authorizeInfoManager, GenerateRefreshTokenStrategy generateRefreshTokenStrategy, CodeStrategy codeStrategy) {
        super(stringRedisTemplate, generateRefreshTokenStrategy);
        this.clientService = clientService;
        this.passwordEncoder = passwordEncoder;
        this.stringRedisTemplate = stringRedisTemplate;
        this.generateTokenStrategy = generateTokenStrategy;
        this.authorizeInfoManager = authorizeInfoManager;
        this.codeStrategy = codeStrategy;
    }

    @Override
    protected TokenInfoDTO doLogic(AuthorizeTokenDTO authorizeTokenDTO, ClientDetailsImpl clientDetails) {
        ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
        String code = authorizeTokenDTO.getCode();
        String clientSecret = authorizeTokenDTO.getClientSecret();
        String clientId = authorizeTokenDTO.getClientId();
        String refreshToken = authorizeTokenDTO.getRefreshToken();
        ThrowUtils.throwIfTrue(StringUtils.isBlank(code), SecurityError.ERROR_CODE);
        ThrowUtils.throwIfTrue(!passwordEncoder.matches(clientSecret, clientDetails.getSecret()), SecurityError.CLIENT_ERROR);

        CodeCacheInfoDTO codeCacheInfoDTO = codeStrategy.get(code);
        ThrowUtils.throwIfTrue(null == codeCacheInfoDTO, SecurityError.ERROR_CODE);

        // 生成token对应的内容
        Map<String, Object> generateTokenParams = buildGenerateTokenParams(
                clientDetails.getTimeout(), codeCacheInfoDTO.getUsername(), codeCacheInfoDTO.getUserId(), clientId, StringUtils.isNotBlank(clientDetails.getPrivateKey()));
        String token = generateTokenStrategy.generate(generateTokenParams);

        // 保存token对应的信息
        List<String> scopes = loadAuthorizedScope(clientId, codeCacheInfoDTO.getUserId(), opsForValue);
        authorizeInfoManager.save(codeCacheInfoDTO.getUserId(), token, clientDetails.getTimeout(), codeCacheInfoDTO.getCustomerInfo(), scopes);

        TokenInfoDTO dto = new TokenInfoDTO();
        dto.setAccessToken(token);
        dto.setExpiresIn(clientDetails.getTimeout());
        // 生成refresh token 如果支持的话
        Long refreshTokenTimeout = clientDetails.getRefreshTokenTimeout();
        if (clientDetails.getGrantTypes().contains(GrantType.refresh_token) && refreshTokenTimeout > 0) {
            String newRefreshToken = generateRefreshToken(refreshToken, codeCacheInfoDTO.getUserId(), token, refreshTokenTimeout);
            dto.setRefreshToken(newRefreshToken);
        }

        // 删除code
        codeStrategy.delete(code);
        return dto;
    }

    @Override
    protected ClientDetailsImpl getClientDetails(String clientId) {
        return clientService.findByClientId(clientId);
    }
}
