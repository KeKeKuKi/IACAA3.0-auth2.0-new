package com.gapache.commons.lock;

/**
 * 公平锁
 *
 * @author HuSen
 * @since 2020/7/30 2:15 下午
 */
public final class FairSync extends AbstractSync {
    private static final long serialVersionUID = 6849297031455456366L;

    @Override
    final void lock() {
        acquire(1);
    }

    @Override
    protected final boolean tryAcquire(int acquires) {
        final Thread current = Thread.currentThread();
        int c = getState();
        if (c == 0) {
            // 如果有在排队的前任（队列下一个节点的线程不是自己）则不会立刻去尝试获得锁
            if (!hasQueuedPredecessors() && compareAndSetState(0, acquires)) {
                setExclusiveOwnerThread(current);
                return true;
            }
        } else if (current == getExclusiveOwnerThread()) {
            int nextc = c + acquires;
            if (nextc < 0) {
                throw new Error("Maximum lock count exceeded");
            }
            setState(nextc);
            return true;
        }
        return false;
    }
}
