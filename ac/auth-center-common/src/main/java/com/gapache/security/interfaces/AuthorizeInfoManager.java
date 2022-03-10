package com.gapache.security.interfaces;

import com.gapache.security.model.CustomerInfo;

import java.util.Collection;

/**
 * @author HuSen
 * @since 2020/8/4 6:16 下午
 */
public interface AuthorizeInfoManager {

    /**
     * 保存授权后的信息
     *
     * @param userId       用户ID
     * @param token        accessToken
     * @param timeout      有效时长
     * @param customerInfo 用户自定义信息
     * @param scopes       作用域
     */
    void save(Long userId, String token, Long timeout, CustomerInfo customerInfo, Collection<String> scopes);

    /**
     * 删除授权后的信息
     *
     * @param token token
     */
    void delete(String token);

    /**
     * info
     *
     * @param token token
     * @return info
     */
    String get(String token);
}
