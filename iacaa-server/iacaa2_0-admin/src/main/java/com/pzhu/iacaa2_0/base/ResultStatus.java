package com.pzhu.iacaa2_0.base;

public enum ResultStatus implements BusinessError {
    /**
     * 成功
     */
    SUCCESS(200,"成功"),
    /**
     * 参数异常
     */
    PARAM_ERROR(201,"参数异常"),
    /**
     * 业务异常
     */
    BIZ_ERROR(202),
    /**
     * 系统异常
     */
    EXCEPTION(101),
    /**
     * 系统异常
     */
    PAYING(50),
    /**
     *
     */
    INNER_ERROR(102);

    /**
     * 编码
     */
    private final int code;

    /**
     * 消息
     */
    private String error;

    ResultStatus(int code) {
        this.code = code;
    }

    ResultStatus(int code, String error) {
        this.code = code;
        this.error = error;
    }

    public void setError(String error) {
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
