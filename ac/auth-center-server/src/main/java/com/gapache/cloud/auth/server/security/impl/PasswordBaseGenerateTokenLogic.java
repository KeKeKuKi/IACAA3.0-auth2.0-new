package com.gapache.cloud.auth.server.security.impl;

import com.gapache.cloud.auth.server.model.AuthorizeTokenDTO;
import com.gapache.cloud.auth.server.model.ClientDetailsImpl;
import com.gapache.cloud.auth.server.model.UserClientRelationDTO;
import com.gapache.cloud.auth.server.model.UserDetailsImpl;
import com.gapache.cloud.auth.server.security.BaseGenerateTokenLogic;
import com.gapache.cloud.auth.server.service.ClientService;
import com.gapache.cloud.auth.server.service.UserClientRelationService;
import com.gapache.cloud.auth.server.service.UserService;
import com.gapache.commons.model.ThrowUtils;
import com.gapache.security.interfaces.AuthorizeInfoManager;
import com.gapache.security.interfaces.GenerateTokenStrategy;
import com.gapache.security.model.SecurityError;
import com.gapache.security.model.TokenInfoDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author HuSen
 * @since 2020/8/5 10:07 上午
 */
@Component("password")
public class PasswordBaseGenerateTokenLogic extends BaseGenerateTokenLogic {

    private final ClientService clientService;
    private final UserService userService;
    private final UserClientRelationService userClientRelationService;
    private final PasswordEncoder passwordEncoder;
    private final GenerateTokenStrategy generateTokenStrategy;
    private final AuthorizeInfoManager authorizeInfoManager;

    public PasswordBaseGenerateTokenLogic(ClientService clientService, UserService userService, UserClientRelationService userClientRelationService, PasswordEncoder passwordEncoder, GenerateTokenStrategy generateTokenStrategy, AuthorizeInfoManager authorizeInfoManager) {
        this.clientService = clientService;
        this.userService = userService;
        this.userClientRelationService = userClientRelationService;
        this.passwordEncoder = passwordEncoder;
        this.generateTokenStrategy = generateTokenStrategy;
        this.authorizeInfoManager = authorizeInfoManager;
    }

    @Override
    protected TokenInfoDTO doLogic(AuthorizeTokenDTO authorizeTokenDTO, ClientDetailsImpl clientDetails) {
        String username = authorizeTokenDTO.getUsername();
        String password = authorizeTokenDTO.getPassword();
        UserDetailsImpl userDetails = (UserDetailsImpl) userService.loadUserByUsername(username);
        UserClientRelationDTO userClientRelationDTO = userClientRelationService.findByUserIdAndClientId(userDetails.getId(), clientDetails.getId());
        ThrowUtils.throwIfTrue(userClientRelationDTO == null, SecurityError.CLIENT_ERROR);
        ThrowUtils.throwIfTrue(!passwordEncoder.matches(password, userDetails.getPassword()), SecurityError.LOGIN_FAIL);

        Map<String, Object> generateTokenParams = buildGenerateTokenParams(clientDetails.getTimeout(), username, userDetails.getId(), clientDetails.getClientId(), StringUtils.isNotBlank(clientDetails.getPrivateKey()));
        String token = generateTokenStrategy.generate(generateTokenParams);

        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        Set<String> scopes = clientDetails.getScopes();
        scopes = scopes.stream().filter(scope -> authorities.contains(new SimpleGrantedAuthority(scope))).collect(Collectors.toSet());
        authorizeInfoManager.save(userDetails.getId(), token, clientDetails.getTimeout(), userDetails.getCustomerInfo(), scopes);

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
