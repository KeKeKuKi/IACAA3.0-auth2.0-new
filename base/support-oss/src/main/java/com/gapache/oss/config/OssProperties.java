package com.gapache.oss.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author HuSen
 * @since 2020/8/28 10:20 上午
 */
@Data
@ConfigurationProperties("gapache.oss")
public class OssProperties {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
}
