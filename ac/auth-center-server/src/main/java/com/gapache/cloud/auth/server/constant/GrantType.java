package com.gapache.cloud.auth.server.constant;

/**
 * @author HuSen
 * @since 2020/7/31 5:06 下午
 */
public enum GrantType {
    // 授权码
    authorization_code,
    // 隐秘
    implicit,
    // 密码
    password,
    // 客户端
    client_credentials,
    // 刷新token
    refresh_token
}
