package com.gapache.vertx.web.zeus;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gapache.vertx.web.utils.ObjectUtils;

import java.lang.reflect.Method;

/**
 * @author HuSen
 * @since 2021/3/3 9:26 上午
 */
public class BodyGenerator {

    public String generating(Method method, Object[] args) {
        String body = null;
        // 方案二 按参数的顺序拼接和解析
        if (args != null && args.length > 0) {
            if (args.length == 1) {
                Object arg0 = args[0];
                if (arg0 != null) {
                    Class<?> arg0Class = arg0.getClass();
                    if (ObjectUtils.isSimpleValue(arg0Class)) {

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("arg0", arg0);
                        body = jsonObject.toJSONString();
                    } else {
                        body = JSON.toJSONString(arg0);
                    }
                }
            } else {
                JSONObject jsonObject = new JSONObject();
                for (int i = 0; i < args.length; i++) {
                    if (args[i] == null) {
                        continue;
                    }
                    Class<?> arg0Class = args[i].getClass();
                    if (ObjectUtils.isSimpleValue(arg0Class)) {
                        jsonObject.put("arg" + i, args[i]);
                    } else {
                        jsonObject.put("arg" + i, JSON.toJSONString(args[i]));
                    }
                }

                body = jsonObject.toJSONString();
            }
        }

        return body;
    }
}
