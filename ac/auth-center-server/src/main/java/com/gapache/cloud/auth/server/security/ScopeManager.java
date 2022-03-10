package com.gapache.cloud.auth.server.security;

import java.util.List;
import java.util.Set;

/**
 * @author HuSen
 * @since 2020/8/6 11:46 上午
 */
public interface ScopeManager {

    /**
     * 保存scope
     *
     * @param clientId 客户端ID
     * @param uid      唯一ID
     * @param scopes   作用域
     * @param timeout  超时时间
     */
    void save(String clientId, Long uid, Long timeout, Set<String> scopes);

    /**
     * 获得scopes
     *
     * @param clientId 客户端ID
     * @param uid      唯一ID
     * @return scopes
     */
    List<String> get(String clientId, Long uid);
}
