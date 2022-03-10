package com.gapache.security.holder;

import com.gapache.security.interfaces.AccessCardHolderStrategy;
import com.gapache.security.model.AccessCard;
import org.springframework.util.Assert;

/**
 * @author HuSen
 * @since 2020/8/9 6:18 下午
 */
public class InheritableThreadLocalAccessCardHolderStrategy implements AccessCardHolderStrategy {

    private static final ThreadLocal<AccessCard> CONTEXT_HOLDER = new InheritableThreadLocal<>();

    @Override
    public void clearContext() {
        CONTEXT_HOLDER.remove();
    }

    @Override
    public AccessCard getContext() {
        AccessCard ctx = CONTEXT_HOLDER.get();

        if (ctx == null) {
            ctx = createEmptyContext();
            CONTEXT_HOLDER.set(ctx);
        }

        return ctx;
    }

    @Override
    public void setContext(AccessCard context) {
        Assert.notNull(context, "Only non-null AccessCard instances are permitted");
        CONTEXT_HOLDER.set(context);
    }

    @Override
    public AccessCard createEmptyContext() {
        return AccessCard.createEmpty();
    }
}
