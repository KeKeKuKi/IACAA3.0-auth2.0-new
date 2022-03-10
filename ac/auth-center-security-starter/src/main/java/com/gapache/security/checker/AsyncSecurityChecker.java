package com.gapache.security.checker;

import com.gapache.security.model.AccessCard;
import io.vertx.core.Future;

/**
 * @author HuSen
 * @since 2020/7/31 12:39 下午
 */
public interface AsyncSecurityChecker {

    /**
     * 检查并解析token
     *
     * @param token token
     * @return 凭证
     */
    Future<AccessCard> checking(String token);
}
