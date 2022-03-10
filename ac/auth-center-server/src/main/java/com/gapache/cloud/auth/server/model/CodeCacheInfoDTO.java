package com.gapache.cloud.auth.server.model;

import com.gapache.security.model.CustomerInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * @author HuSen
 * @since 2020/8/4 10:45 上午
 */
@Data
public class CodeCacheInfoDTO implements Serializable {
    private static final long serialVersionUID = -4869381251459868238L;

    private Long userId;
    private String username;
    private CustomerInfo customerInfo;
}
