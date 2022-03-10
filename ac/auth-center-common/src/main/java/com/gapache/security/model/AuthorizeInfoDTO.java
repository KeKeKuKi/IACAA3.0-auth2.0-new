package com.gapache.security.model;

import lombok.Data;

import java.util.Collection;

/**
 * @author HuSen
 * @since 2020/8/4 11:16 上午
 */
@Data
public class AuthorizeInfoDTO {
    private Collection<String> scopes;
    private CustomerInfo customerInfo;
}
