package com.gapache.commons.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author HuSen
 * @since 2021/1/18 10:38 上午
 */
@Data
public class ResponseHead implements Serializable {
    private static final long serialVersionUID = -5532480215283747616L;

    /**
     * 版本号
     */
    private String version;

    /**
     * 应用ID
     */
    private String appid;

    /**
     * 接口代码
     */
    private String function;

    /**
     * 报文发起时间
     */
    private String respTime;

    /**
     * 报文发起时区
     */
    private String respTimeZone;

    /**
     * 请求报文ID
     */
    private String reqMsgId;

    /**
     * 报文字符编码
     */
    private String inputCharset;

    /**
     * 保留字段
     */
    private String reserve;

    /**
     * 签名方式
     */
    private String signType;
}
