package com.gapache.cloud.auth.server.security.impl;

import com.gapache.cloud.auth.server.model.AuthorizeTokenDTO;
import com.gapache.cloud.auth.server.model.ClientDetailsImpl;
import com.gapache.security.interfaces.AuthorizeInfoManager;
import com.gapache.cloud.auth.server.security.BaseGenerateTokenLogic;
import com.gapache.cloud.auth.server.service.ClientService;
import com.gapache.commons.model.ThrowUtils;
import com.gapache.security.interfaces.GenerateTokenStrategy;
import com.gapache.security.model.SecurityError;
import com.gapache.security.model.TokenInfoDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

/**
 * @author HuSen
 * @since 2020/8/5 10:31 上午
 */
@Component("client_credentials")
public class ClientCredentialsGenerateTokenLogic extends BaseGenerateTokenLogic {

    private final ClientService clientService;
    private final AuthorizeInfoManager authorizeInfoManager;
    private final PasswordEncoder passwordEncoder;
    private final GenerateTokenStrategy generateTokenStrategy;

    public ClientCredentialsGenerateTokenLogic(ClientService clientService, AuthorizeInfoManager authorizeInfoManager, PasswordEncoder passwordEncoder, GenerateTokenStrategy generateTokenStrategy) {
        this.clientService = clientService;
        this.authorizeInfoManager = authorizeInfoManager;
        this.passwordEncoder = passwordEncoder;
        this.generateTokenStrategy = generateTokenStrategy;
    }

    @Override
    protected TokenInfoDTO doLogic(AuthorizeTokenDTO authorizeTokenDTO, ClientDetailsImpl clientDetails) {
        String clientId = authorizeTokenDTO.getClientId();
        String clientSecret = authorizeTokenDTO.getClientSecret();
        ThrowUtils.throwIfTrue(!passwordEncoder.matches(clientSecret, clientDetails.getSecret()), SecurityError.CLIENT_ERROR);
        Map<String, Object> generateTokenParams = buildGenerateTokenParams(clientDetails.getTimeout(), clientId, clientDetails.getId(), clientId, StringUtils.isNotBlank(clientDetails.getPrivateKey()));
        String token = generateTokenStrategy.generate(generateTokenParams);
        Set<String> scopes = clientDetails.getScopes();
        authorizeInfoManager.save(-1L, token, clientDetails.getTimeout(), null, scopes);

        TokenInfoDTO dto = new TokenInfoDTO();
        dto.setAccessToken(token);
        dto.setExpiresIn(clientDetails.getTimeout());
        return dto;
    }

    @Override
    protected ClientDetailsImpl getClientDetails(String clientId) {
        return clientService.findByClientId(clientId);
    }
}
