package com.gapache.commons.model;

import lombok.Getter;

/**
 * 全局响应结果，采用状态机设计。
 * 此响应结果仅仅适用于全局通用的响应结果，针对具体业务的响应，应当采取根据业务类自身定义的 {Business}Response。
 * 从 220000（成功） 开始， 到 229999（服务器错误） 结束，中间的四位数字根据业务定义。
 * 每隔 5 个数字作为一个状态机，特殊状态可以采取 1 ~ 5 之间的中间状态值。
 *
 * @author HuSen
 * @since 2021/1/18 10:42 上午
 */
@Getter
public enum GlobalResponse implements BaseResponse {
    /**
     * 处理成功
     */
    SUCCESS(220000, "处理成功"),

    /**
     * 处理失败
     */
    FAILED(220000, "处理失败"),

    /**
     * 无效的TOKEN
     */
    INVALID_TOKEN(220000, "无效的TOKEN"),

    /**
     * 无效的参数
     */
    INVALID_PARAM(220000, "无效的参数"),

    /**
     * 服务器错误
     */
    ERROR(229999, "服务器错误");

    GlobalResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 响应代码
     */
    private final int code;

    /**
     * 响应消息
     */
    private final String message;
}
