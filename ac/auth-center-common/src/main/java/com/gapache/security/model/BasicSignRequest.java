package com.gapache.security.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 如果接口要进行签名校验则需要继承此类
 * 签名规则
 * 1.将业务请求参数和时间戳按照字母排序顺序用键值对的方式拼接起来，在前后拼接上clientId，使用签名算法进行签名
 * 2.bytes = sign(clientIda=1b=2timestamp=now()clientId, clientSecret)
 * 3.str = base64(bytes)
 * 4.str全转大写即为最终的签名
 *
 * @author HuSen
 * @since 2021/1/25 3:21 下午
 */
@Getter
@Setter
@ToString
public class BasicSignRequest implements Serializable {
    private static final long serialVersionUID = -7159463893062888822L;

    /**
     * 签名值，不参与签名的计算
     */
    private String sign;

    /**
     * ID
     */
    private String clientId;

    /**
     * 当前时间戳，请求5分钟内有效
     */
    private Long timestamp;
}
