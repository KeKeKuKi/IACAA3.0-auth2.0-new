package com.gapache.cloud.auth.server.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author HuSen
 * @since 2020/8/4 10:21 上午
 */
@Data
public class UserAuthorizeDTO implements Serializable {
    private static final long serialVersionUID = -3291230032181953188L;

    private String scopes;

    private String redirectUrl;
}
