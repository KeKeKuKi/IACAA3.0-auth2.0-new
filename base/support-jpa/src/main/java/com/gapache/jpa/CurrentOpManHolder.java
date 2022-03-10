package com.gapache.jpa;

/**
 * @author HuSen
 * @since 2021/2/1 1:37 下午
 */
public final class CurrentOpManHolder {

    private static final ThreadLocal<Long> CONTEXT_HOLDER = new InheritableThreadLocal<>();

    public static Long getCurrent() {
        Long current = CONTEXT_HOLDER.get();
        if (current == null) {
            return createEmpty();
        }
        return current;
    }

    public static void setCurrent(Long current) {
        CONTEXT_HOLDER.set(current);
    }

    private static Long createEmpty() {
        return 0L;
    }

    public static void clear() {
        CONTEXT_HOLDER.remove();
    }
}
