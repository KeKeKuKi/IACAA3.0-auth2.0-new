package com.gapache.security.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author HuSen
 * @since 2020/7/31 1:10 下午
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "com.gapache.security")
public class SecurityProperties {
    /**
     * 私钥
     */
    private String privateKey;
    /**
     * 公钥
     */
    private String publicKey;
    /**
     * 超时时间
     */
    private Long timeout = 36000000L;

    // 分割

    /**
     * 是否进行远程注册
     */
    private Boolean registerRemote = false;
    /**
     * 是否更新资源
     */
    private Boolean updateResources = true;
    /**
     * 分配的clientId
     */
    private String clientId;
}
