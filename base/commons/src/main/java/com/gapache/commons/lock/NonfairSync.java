package com.gapache.commons.lock;

/**
 * 非公平锁
 *
 * @author HuSen
 * @since 2020/7/30 2:06 下午
 */
public final class NonfairSync extends AbstractSync {
    private static final long serialVersionUID = 4140946380872770357L;

    /**
     * 执行锁定。
     * 立即尝试获取，失败时备份到正常获取。
     */
    @Override
    final void lock() {
        if (compareAndSetState(0, 1)) {
            setExclusiveOwnerThread(Thread.currentThread());
        } else {
            acquire(1);
        }
    }

    /**
     * aqs要求我们要自己实现这个方法
     *
     * @param acquires 获取锁的次数
     * @return 是否获取成功
     */
    @Override
    protected final boolean tryAcquire(int acquires) {
        return nonfairTryAcquire(acquires);
    }
}
