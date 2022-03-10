package com.gapache.security.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author HuSen
 * @since 2020/8/3 11:29 上午
 */
@Data
public class ClientDTO implements Serializable {
    private static final long serialVersionUID = -8023517195045962090L;

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
}
