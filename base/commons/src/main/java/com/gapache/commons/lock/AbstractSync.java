package com.gapache.commons.lock;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * @author HuSen
 * @since 2020/7/30 1:49 下午
 */
public abstract class AbstractSync extends AbstractQueuedSynchronizer {
    private static final long serialVersionUID = 1717547259579380305L;

    abstract void lock();

    final boolean nonfairTryAcquire(int acquires) {
        // 先获取当前线程
        final Thread current = Thread.currentThread();
        // 获取当前的状态
        int c = getState();
        // 为0的话表示锁还没有被持有，尝试使用cas进行获取锁
        if (c == 0) {
            if (compareAndSetState(0, acquires)) {
                // 获取到锁的线程将当前独占的线程设置为自己
                setExclusiveOwnerThread(current);
            }
        }
        // 可重入，只有持有锁的线程才会进进这里
        else if (current == getExclusiveOwnerThread()) {
            int nextc = c + acquires;
            // overflow
            if (nextc < 0) {
                throw new Error("Maximum lock count exceeded");
            }
            setState(nextc);
            return true;
        }
        return false;
    }

    @Override
    protected final boolean tryRelease(int releases) {
        // 当前剩余加锁次数
        int c = getState() - releases;
        // 只有独占锁的线程才能进行释放的操作
        if (Thread.currentThread() != getExclusiveOwnerThread()) {
            throw new IllegalMonitorStateException();
        }
        boolean free = false;
        // 加锁次数为0时，表示所有的锁已经释放了
        if (c == 0) {
            free = true;
            setExclusiveOwnerThread(null);
        }
        setState(c);
        return free;
    }

    @Override
    protected final boolean isHeldExclusively() {
        return getExclusiveOwnerThread() == Thread.currentThread();
    }

    final ConditionObject newCondition() {
        return new ConditionObject();
    }

    final Thread getOwner() {
        return getState() == 0 ? null : getExclusiveOwnerThread();
    }

    final int getHoldCount() {
        return isHeldExclusively() ? getState() : 0;
    }

    final boolean isLocked() {
        return getState() != 0;
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        setState(0);
    }
}
