package com.gapache.security.checker;

import com.alibaba.fastjson.JSONObject;

/**
 * @author HuSen
 * @since 2021/2/1 4:19 下午
 */
public interface SignChecker {

    boolean checkSign(JSONObject body, String clientId);
}
