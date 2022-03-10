package com.gapache.cloud.auth.server.model;

import com.gapache.cloud.auth.server.constant.GrantType;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author HuSen
 * @since 2020/7/31 4:50 下午
 */
@Setter
public class ClientDetailsImpl {
    private Long id;
    private String clientId;
    private String secret;
    private String grantTypes;
    private String scopes;
    private String redirectUrl;
    private Long timeout;
    private Long refreshTokenTimeout;
    private Boolean autoGrant;
    private String privateKey;

    public String getPrivateKey() {
        return privateKey;
    }

    public Set<GrantType> getGrantTypes() {
        if (StringUtils.isBlank(grantTypes)) {
            return new HashSet<>(0);
        }
        return Arrays.stream(grantTypes.split(",")).map(GrantType::valueOf).collect(Collectors.toSet());
    }

    public Set<String> getScopes() {
        if (StringUtils.isBlank(scopes)) {
            return new HashSet<>(0);
        }
        return Arrays.stream(scopes.split(",")).collect(Collectors.toSet());
    }

    public Long getId() {
        return id;
    }

    public String getClientId() {
        return clientId;
    }

    public String getSecret() {
        return secret;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public Long getTimeout() {
        return timeout;
    }

    public Long getRefreshTokenTimeout() {
        return refreshTokenTimeout;
    }

    public Boolean getAutoGrant() {
        return autoGrant;
    }
}
