package com.gapache.commons.model;

/**
 * @author HuSen
 * create on 2020/1/11 11:37
 */
public interface Error {

    /**
     * 返回错误码
     *
     * @return 错误码
     */
    Integer getCode();

    /**
     * 返回错误的描述
     *
     * @return 错误的描述
     */
    String getError();
}
