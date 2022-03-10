package com.gapache.commons.model;

/**
 * @author HuSen
 * @since 2020/11/19 2:06 下午
 */
public enum  CommonError implements Error {
    //
    PARAM_ERROR(999998, "参数错误");

    private final int code;
    private final String error;

    CommonError(int code, String error) {
        this.code = code;
        this.error = error;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getError() {
        return error;
    }
}
