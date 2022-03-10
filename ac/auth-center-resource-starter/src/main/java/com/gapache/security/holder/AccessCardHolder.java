package com.gapache.security.holder;

import com.gapache.security.interfaces.AccessCardHolderStrategy;
import com.gapache.security.model.AccessCard;

/**
 * @author HuSen
 * @since 2020/8/9 6:26 下午
 */
public class AccessCardHolder {

    private static final AccessCardHolderStrategy STRATEGY = new InheritableThreadLocalAccessCardHolderStrategy();

    public static void clearContext() {
        STRATEGY.clearContext();
    }

    public static AccessCard getContext() {
        AccessCard context = STRATEGY.getContext();
        if (context == null) {
            context = createEmptyContext();
        }
        return context;
    }

    public static void setContext(AccessCard context) {
        STRATEGY.setContext(context);
    }

    public static AccessCard createEmptyContext() {
        return STRATEGY.createEmptyContext();
    }
}
