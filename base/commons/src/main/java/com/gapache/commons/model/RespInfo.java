package com.gapache.commons.model;

import lombok.Data;

/**
 * @author HuSen
 * @since 2021/1/18 10:40 上午
 */
@Data
public class RespInfo implements RespInfoHelper {

    /**
     * 结果状态（S：成功，F：失败，U：未知）
     */
    private String resultStatus;

    /**
     * 结果代码，
     * 当resultStatus为S时，该字段必定为0000。
     * 当resultStatus为F或U时，该字段可以为全局返回码，也可以为业务返回码。
     * 如果为业务返回码，参见业务接口部分。
     */
    private String resultCode;

    /**
     * 结果消息，
     * 当resultStatus为S时，该字段可为空。
     * 当resultStatus为F或U时，需要描述该错误的原因。
     */
    private String resultMsg;

    /**
     * 构造器
     */
    public RespInfo() {
    }

    /**
     * 构造器
     * @param resultStatus 结果状态（S：成功，F：失败，U：未知）
     * @param resultCode 结果代码
     * @param resultMsg 结果消息
     */
    public RespInfo(String resultStatus, String resultCode, String resultMsg) {
        this.resultStatus = resultStatus;
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }

    @Override
    public boolean isSuccess() {
        return S.equalsIgnoreCase(getResultStatus());
    }

    @Override
    public boolean isFailed() {
        return F.equalsIgnoreCase(getResultStatus());
    }

    @Override
    public boolean isUnknown() {
        return U.equalsIgnoreCase(getResultStatus());
    }
}
