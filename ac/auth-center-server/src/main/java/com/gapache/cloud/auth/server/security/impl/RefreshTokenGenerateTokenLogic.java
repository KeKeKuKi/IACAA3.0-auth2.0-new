package com.gapache.cloud.auth.server.security.impl;

import com.alibaba.fastjson.JSON;
import com.gapache.cloud.auth.server.constant.GrantType;
import com.gapache.cloud.auth.server.model.*;
import com.gapache.cloud.auth.server.security.BaseGenerateTokenLogic;
import com.gapache.cloud.auth.server.security.GenerateRefreshTokenStrategy;
import com.gapache.cloud.auth.server.service.ClientService;
import com.gapache.cloud.auth.server.service.UserClientRelationService;
import com.gapache.cloud.auth.server.service.UserService;
import com.gapache.commons.model.ThrowUtils;
import com.gapache.security.interfaces.AuthorizeInfoManager;
import com.gapache.security.interfaces.GenerateTokenStrategy;
import com.gapache.security.model.SecurityError;
import com.gapache.security.model.TokenInfoDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.gapache.cloud.auth.server.constant.RedisConstants.REFRESH_TOKEN_PREFIX;

/**
 * @author HuSen
 * @since 2020/8/5 10:34 上午
 */
@Component("refresh_token")
public class RefreshTokenGenerateTokenLogic extends BaseGenerateTokenLogic {

    private final ClientService clientService;
    private final PasswordEncoder passwordEncoder;
    private final StringRedisTemplate stringRedisTemplate;
    private final UserService userService;
    private final UserClientRelationService userClientRelationService;
    private final GenerateTokenStrategy generateTokenStrategy;
    private final AuthorizeInfoManager authorizeInfoManager;

    public RefreshTokenGenerateTokenLogic(ClientService clientService, PasswordEncoder passwordEncoder, StringRedisTemplate stringRedisTemplate, UserService userService, UserClientRelationService userClientRelationService, GenerateTokenStrategy generateTokenStrategy, AuthorizeInfoManager authorizeInfoManager, GenerateRefreshTokenStrategy generateRefreshTokenStrategy) {
        super(stringRedisTemplate, generateRefreshTokenStrategy);
        this.clientService = clientService;
        this.passwordEncoder = passwordEncoder;
        this.stringRedisTemplate = stringRedisTemplate;
        this.userService = userService;
        this.userClientRelationService = userClientRelationService;
        this.generateTokenStrategy = generateTokenStrategy;
        this.authorizeInfoManager = authorizeInfoManager;
    }

    @Override
    protected TokenInfoDTO doLogic(AuthorizeTokenDTO authorizeTokenDTO, ClientDetailsImpl clientDetails) {
        String refreshToken = authorizeTokenDTO.getRefreshToken();
        String clientSecret = authorizeTokenDTO.getClientSecret();
        String clientId = authorizeTokenDTO.getClientId();
        ThrowUtils.throwIfTrue(StringUtils.isBlank(refreshToken), SecurityError.NEED_REFRESH_TOKEN);
        ThrowUtils.throwIfTrue(!passwordEncoder.matches(clientSecret, clientDetails.getSecret()), SecurityError.CLIENT_ERROR);

        String refreshTokenDtoStr = stringRedisTemplate.opsForValue().get(REFRESH_TOKEN_PREFIX + refreshToken);
        ThrowUtils.throwIfTrue(StringUtils.isBlank(refreshTokenDtoStr), SecurityError.REFRESH_TOKEN_EXPIRED);

        RefreshTokenDTO refreshTokenDTO = JSON.parseObject(refreshTokenDtoStr, RefreshTokenDTO.class);
        UserDetailsImpl userDetails = userService.findById(refreshTokenDTO.getUserId());

        UserClientRelationDTO userClientRelationDTO = userClientRelationService.findByUserIdAndClientId(userDetails.getId(), clientDetails.getId());
        ThrowUtils.throwIfTrue(userClientRelationDTO == null, SecurityError.CLIENT_ERROR);

        // 删除旧token
        authorizeInfoManager.delete(refreshTokenDTO.getAccessToken());

        Map<String, Object> generateTokenParams = buildGenerateTokenParams(clientDetails.getTimeout(), userDetails.getUsername(), userDetails.getId(), clientId,
                StringUtils.isNotBlank(clientDetails.getPrivateKey()));
        String token = generateTokenStrategy.generate(generateTokenParams);

        List<String> scopes = loadAuthorizedScope(clientId, userDetails.getId(), stringRedisTemplate.opsForValue());
        authorizeInfoManager.save(userDetails.getId(), token, clientDetails.getTimeout(), userDetails.getCustomerInfo(), scopes);

        TokenInfoDTO dto = new TokenInfoDTO();
        dto.setAccessToken(token);
        dto.setExpiresIn(clientDetails.getTimeout());
        // 生成refresh token 如果支持的话
        Long refreshTokenTimeout = clientDetails.getRefreshTokenTimeout();
        if (clientDetails.getGrantTypes().contains(GrantType.refresh_token) && refreshTokenTimeout > 0) {
            String newRefreshToken = generateRefreshToken(refreshToken, userDetails.getId(), token, refreshTokenTimeout);
            dto.setRefreshToken(newRefreshToken);
        }
        return dto;
    }

    @Override
    protected ClientDetailsImpl getClientDetails(String clientId) {
        return clientService.findByClientId(clientId);
    }
}
