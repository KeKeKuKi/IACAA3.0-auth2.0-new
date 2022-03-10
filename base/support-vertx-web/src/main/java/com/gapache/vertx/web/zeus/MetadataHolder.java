package com.gapache.vertx.web.zeus;

import java.util.Map;

/**
 * @author HuSen
 * @since 2021/3/4 4:08 下午
 */
public interface MetadataHolder {

    /**
     * 设置Metadata
     *
     * @param metadata 元数据
     */
    void setMetadata(Map<String, String> metadata);
}
