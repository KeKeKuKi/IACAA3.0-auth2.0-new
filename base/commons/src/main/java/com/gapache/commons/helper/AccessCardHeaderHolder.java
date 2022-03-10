package com.gapache.commons.helper;

/**
 * @author HuSen
 * @since 2021/2/1 3:19 下午
 */
public final class AccessCardHeaderHolder {

    private static final ThreadLocal<String> CONTEXT_HOLDER = new InheritableThreadLocal<>();

    public static String getHeader() {
        return CONTEXT_HOLDER.get();
    }

    public static void setHeader(String header) {
        CONTEXT_HOLDER.set(header);
    }

    public static void clear() {
        CONTEXT_HOLDER.remove();
    }
}
