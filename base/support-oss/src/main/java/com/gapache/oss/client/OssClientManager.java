package com.gapache.oss.client;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.Getter;

/**
 * @author HuSen
 * @since 2020/8/28 10:27 上午
 */
@Getter
public class OssClientManager {

    private final String endpoint;
    private final String accessKeyId;
    private final String accessKeySecret;

    public OssClientManager(String endpoint, String accessKeyId, String accessKeySecret) {
        this.endpoint = endpoint;
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
    }

    public OSS getOss() {
        return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }
}
