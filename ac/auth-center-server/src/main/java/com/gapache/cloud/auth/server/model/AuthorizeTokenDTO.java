package com.gapache.cloud.auth.server.model;

import com.gapache.cloud.auth.server.constant.GrantType;
import lombok.Data;

import java.io.Serializable;

/**
 * @author HuSen
 * @since 2020/8/5 9:41 上午
 */
@Data
public class AuthorizeTokenDTO implements Serializable {
    private static final long serialVersionUID = 6674748019705583198L;

    private GrantType grantType;

    private String clientId;

    private String clientSecret;

    private String code;

    private String refreshToken;

    private String username;

    private String password;
}
