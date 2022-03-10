package com.gapache.security.interfaces;

import com.gapache.security.model.AccessCard;

/**
 * 解析token的策略
 *
 * @author HuSen
 * @since 2020/8/5 1:41 下午
 */
public interface ParseTokenStrategy {

    /**
     * 解析token对应的content
     *
     * @param content content
     * @return AccessCard
     */
    AccessCard parse(String content);
}
