package com.gapache.cloud.auth.server.constant;

/**
 * @author HuSen
 * @since 2020/7/31 5:23 下午
 */
public enum ResponseType {
    // 要求返回授权码
    code,
    // 要求直接返回token（隐密式）
    token
}
