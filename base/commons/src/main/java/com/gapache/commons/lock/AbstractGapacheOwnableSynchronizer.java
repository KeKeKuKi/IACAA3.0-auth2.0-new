package com.gapache.commons.lock;

import java.io.Serializable;

/**
 * 抽象的当前独占的线程的同步器
 *
 * @author HuSen
 * @since 2020/7/29 7:47 下午
 */
public abstract class AbstractGapacheOwnableSynchronizer implements Serializable {
    private static final long serialVersionUID = 5434878576818575861L;

    protected AbstractGapacheOwnableSynchronizer() {}

    private transient Thread exclusiveOwnerThread;

    protected final void setExclusiveOwnerThread(Thread thread) {
        this.exclusiveOwnerThread = thread;
    }

    protected final Thread getExclusiveOwnerThread() {
        return exclusiveOwnerThread;
    }
}
