package com.gapache.commons.sdk;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.nio.charset.StandardCharsets;

/**
 * @author HuSen
 * @since 2020/6/24 3:23 下午
 */
public abstract class AbstractSdk implements Sdk {

    private final String paramPath;

    public AbstractSdk(String paramPath) {
        this.paramPath = paramPath;
    }

    @Override
    public void init() {
        ClassPathResource resource = new ClassPathResource(paramPath);
        try {
            byte[] bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
            String config = new String(bytes, StandardCharsets.UTF_8);
            doInitLogic(config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void reload() {
        ClassPathResource resource = new ClassPathResource(paramPath);
        try {
            byte[] bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
            String config = new String(bytes, StandardCharsets.UTF_8);
            doReloadLogic(config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 重新加载完成以后的逻辑实现
     *
     * @param config 解析出来的配置
     */
    protected abstract void doReloadLogic(String config);

    /**
     * 解析完成以后的逻辑实现
     *
     * @param config 解析出来的配置
     */
    protected abstract void doInitLogic(String config);
}
