package com.gapache.commons.model;

import lombok.Getter;

/**
 * @author HuSen
 * @since 2020/8/3 10:21 上午
 */
@Getter
public enum SystemError implements Error {
    //
    SERVER_EXCEPTION(999999, "system error"),
    SERVER_FLOW(999998, "接口限流了"),
    SERVER_DEGRADE(999997, "服务降级了"),
    SERVER_PARAM_FLOW(999996, "热点参数限流了"),
    SERVER_SYSTEM_BLOCK(999995, "系统规则（负载/...不满足要求）"),
    SERVER_AUTHORITY(999994, "授权规则不通过");

    private final Integer code;
    private final String error;

    SystemError(Integer code, String error) {
        this.code = code;
        this.error = error;
    }
}
