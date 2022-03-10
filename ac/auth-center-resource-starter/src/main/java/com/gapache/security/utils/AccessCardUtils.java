package com.gapache.security.utils;

import com.gapache.commons.helper.AccessCardHeaderHolder;
import com.gapache.commons.model.AuthConstants;
import com.gapache.protobuf.utils.ProtocstuffUtils;
import com.gapache.security.holder.AccessCardHolder;
import com.gapache.security.model.AccessCard;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Base64Utils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @author HuSen
 * @since 2021/3/24 3:12 下午
 */
@Slf4j
public class AccessCardUtils {

    public static final String FILTER_APPLIED = "__acpf_applied";
    private static Method setCurrent;
    private static Method clear;

    static {
        try {
            Class<?> jpaCurrentOpManHolder = Class.forName("com.gapache.jpa.CurrentOpManHolder");
            setCurrent = jpaCurrentOpManHolder.getMethod("setCurrent", Long.class);
            clear = jpaCurrentOpManHolder.getMethod("clear");
            Method.setAccessible(new Method[]{setCurrent, clear}, true);
        } catch (Exception ignored) {}
    }

    public static void checkAccessCard(HttpServletRequest request) {
        if (request.getAttribute(FILTER_APPLIED) != null) {
            // 确保一次请求中这个filter只应用一次
            return;
        }

        request.setAttribute(FILTER_APPLIED, Boolean.TRUE);

        String encoded = request.getHeader(AuthConstants.ACCESS_CARD_HEADER);
        if (StringUtils.isBlank(encoded)) {
            // 在ACCESS_CARD_HEADER为空的情况下，判断请求来源
            String from = request.getHeader(AuthConstants.X_FROM_HEADER);
            if (StringUtils.isNotBlank(from)) {
                AccessCard accessCard = new AccessCard();
                accessCard.setUsername(from);
                accessCard.setUserId(0L);
                accessCard.setAuthorities(Sets.newHashSet());
                accessCard.setClientId(from);
                setCurrent(accessCard);
                AccessCardHolder.setContext(accessCard);
            }
            return;
        }
        byte[] bytes = Base64Utils.decodeFromString(encoded);
        AccessCard accessCard = ProtocstuffUtils.byte2Bean(bytes, AccessCard.class);
        log.info("get access card from header:{}", accessCard);
        AccessCardHolder.setContext(accessCard);
        // 设置当前的jpa操作人员标识，如果有的话
        setCurrent(accessCard);
        // 保存当前的access card请求头
        AccessCardHeaderHolder.setHeader(encoded);
    }

    public static void clearCurrentOpMan() {
        try {
            clear.invoke(null);
        } catch (Exception ignored) {}
    }

    private static void setCurrent(AccessCard accessCard) {
        try {
            if (setCurrent != null) {
                setCurrent.invoke(null, accessCard.getUserId());
            }
        } catch (Exception ignored) {}
    }
}
