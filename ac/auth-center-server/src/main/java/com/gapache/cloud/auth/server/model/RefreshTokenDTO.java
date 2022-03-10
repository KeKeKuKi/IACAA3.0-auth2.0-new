package com.gapache.cloud.auth.server.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author HuSen
 * @since 2020/8/3 10:33 上午
 */
@Data
public class RefreshTokenDTO implements Serializable {
    private static final long serialVersionUID = 4970429787063539266L;

    private Long userId;

    private String accessToken;
}
