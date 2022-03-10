package com.gapache.security.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author HuSen
 * @since 2020/7/31 5:57 下午
 */
@Data
public class TokenInfoDTO implements Serializable {
    private static final long serialVersionUID = -5235937523375546667L;

    private String accessToken;
    private String refreshToken;
    private Long expiresIn;
}
