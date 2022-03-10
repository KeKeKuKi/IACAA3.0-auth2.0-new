package com.gapache.security.checker;

import com.gapache.security.model.AccessCard;
import com.gapache.security.model.Certification;

/**
 * @author HuSen
 * @since 2020/7/31 12:39 下午
 */
public interface SecurityChecker {

    /**
     * 检查并解析token
     *
     * @param token token
     * @return 凭证
     */
    AccessCard checking(String token);
}
