package com.gapache.commons.security;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * HmacSHA256签名
 *
 * @author HuSen
 * @since 2021/1/25 3:29 下午
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public class HMACSHA256Utils {

    public static String sign(String data, String key) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKeySpec);

            byte[] bytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return HexUtil.byteArrayToHexString(bytes).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
