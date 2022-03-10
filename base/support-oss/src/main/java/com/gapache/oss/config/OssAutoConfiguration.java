package com.gapache.oss.config;

import com.gapache.oss.OssTemplate;
import com.gapache.oss.client.OssClientManager;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

/**
 * @author HuSen
 * @since 2020/8/28 10:23 上午
 */
@Configuration
@EnableConfigurationProperties(OssProperties.class)
public class OssAutoConfiguration {

    private final OssProperties ossProperties;

    public OssAutoConfiguration(OssProperties ossProperties) {
        this.ossProperties = ossProperties;
    }

    @Bean
    public OssClientManager ossClientManager() {
        String endpoint = ossProperties.getEndpoint();
        String accessKeyId = ossProperties.getAccessKeyId();
        String accessKeySecret = ossProperties.getAccessKeySecret();
        Assert.hasText(endpoint, "require endpoint");
        Assert.hasText(accessKeyId, "require accessKeyId");
        Assert.hasText(accessKeySecret, "require accessKeySecret");
        return new OssClientManager(endpoint, accessKeyId, accessKeySecret);
    }

    @Bean
    public OssTemplate ossTemplate(OssClientManager ossClientManager) {
        return new OssTemplate(ossClientManager);
    }
}
