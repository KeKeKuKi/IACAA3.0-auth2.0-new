package com.gapache.security.interfaces;

import com.gapache.security.model.AccessCard;

/**
 * @author HuSen
 * @since 2020/8/9 6:04 下午
 */
public interface AccessCardHolderStrategy {

    /**
     * Clears the current context.
     */
    void clearContext();

    /**
     * Obtains the current context.
     *
     * @return a context (never <code>null</code> - create a default implementation if
     * necessary)
     */
    AccessCard getContext();

    /**
     * Sets the current context.
     *
     * @param context to the new argument (should never be <code>null</code>, although
     * implementations must check if <code>null</code> has been passed and throw an
     * <code>IllegalArgumentException</code> in such cases)
     */
    void setContext(AccessCard context);

    /**
     * Creates a new, empty context implementation, for use by
     * <tt>SecurityContextRepository</tt> implementations, when creating a new context for
     * the first time.
     *
     * @return the empty context.
     */
    AccessCard createEmptyContext();
}
