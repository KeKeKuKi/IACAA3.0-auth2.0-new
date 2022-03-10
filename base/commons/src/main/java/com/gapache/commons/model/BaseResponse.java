package com.gapache.commons.model;

/**
 * @author HuSen
 * @since 2021/1/18 10:41 上午
 */
public interface BaseResponse {

    /**
     * 获取响应代码
     *
     * @return 返回响应代码
     */
    int getCode();

    /**
     * 获取响应消息
     *
     * @return 返回响应消息
     */
    String getMessage();
}
