package com.gapache.user.server.model;

import com.gapache.commons.model.Error;

/**
 * @author HuSen
 * create on 2020/3/31 10:40 上午
 */
public enum AccountError implements Error {
    //
    PHONE_OR_PASSWORD_WRONG(10001, "手机号或密码错误!");

    private int code;
    private String error;

    AccountError(int code, String error) {
        this.code = code;
        this.error = error;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getError() {
        return this.error;
    }
}
