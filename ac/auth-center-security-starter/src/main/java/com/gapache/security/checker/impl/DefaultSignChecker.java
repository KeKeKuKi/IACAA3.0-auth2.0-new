package com.gapache.security.checker.impl;

import com.alibaba.fastjson.JSONObject;
import com.gapache.commons.security.RSAUtils;
import com.gapache.commons.utils.IStringUtils;
import com.gapache.security.checker.SignChecker;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author HuSen
 * @since 2021/2/1 4:20 下午
 */
@Slf4j
public class DefaultSignChecker implements SignChecker {

    private static final String SIGN_FIELD = "sign";

    private final StringRedisTemplate stringRedisTemplate;

    public DefaultSignChecker(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean checkSign(JSONObject body, String clientId) {
        String privateKey = stringRedisTemplate.opsForValue().get("CLIENT_PRIVATE_KEY:" + clientId);
        if (StringUtils.isBlank(privateKey)) {
            return false;
        }

        String sign = body.getString(SIGN_FIELD);
        Set<String> keySet = body.keySet();
        // 利用TreeMap来存储，键的顺序会自动排好
        Map<String, Object> treeMap = new TreeMap<>();
        keySet.forEach(key -> {
            Object o = body.get(key);
            // null的不需要参与签名计算 sign也不需要参与计算
            if (o != null && !StringUtils.equals(SIGN_FIELD, key)) {
                treeMap.put(key, o);
            }
        });
        StringBuilder sb = new StringBuilder();
        sb.append(clientId);
        treeMap.forEach((k, o) -> sb.append(k).append(o.toString()));
        sb.append(clientId);

        byte[] decryptSign;
        try {
            decryptSign = RSAUtils.decrypt(IStringUtils.getBytes(sign), RSAUtils.getPrivateKey(privateKey));
        } catch (Exception e) {
            log.error("私钥非法.", e);
            return false;
        }
        String check = IStringUtils.newString(decryptSign);

        // 判断内容是否相同
        return StringUtils.equals(check, sb.toString());
    }
}
