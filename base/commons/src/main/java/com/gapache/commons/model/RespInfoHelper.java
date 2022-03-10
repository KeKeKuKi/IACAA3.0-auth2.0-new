package com.gapache.commons.model;

/**
 * @author HuSen
 * @since 2021/1/18 10:39 上午
 */
public interface RespInfoHelper {

    /**
     * 成功标识
     */
    String S = "S";

    /**
     * 失败标识
     */
    String F = "F";

    /**
     * 未知标识
     */
    String U = "U";

    /**
     * 成功状态下的代码
     */
    String CODE_SUCCESS = "0000";

    /**
     * 成功响应
     */
    RespInfo SUCCESS = new RespInfo(S, CODE_SUCCESS, GlobalResponse.SUCCESS.getMessage());

    /**
     * 失败响应
     */
    RespInfo FAILED = new RespInfo(F, String.valueOf(GlobalResponse.FAILED.getCode()), GlobalResponse.FAILED.getMessage());

    /**
     * 是否成功
     *
     * @return 如果成功返回true，否则返回false
     */
    boolean isSuccess();

    /**
     * 是否失败
     *
     * @return 如果失败返回true，否则返回false
     */
    boolean isFailed();

    /**
     * 是否未知
     *
     * @return 如果未知返回true，否则返回false
     */
    boolean isUnknown();
}
