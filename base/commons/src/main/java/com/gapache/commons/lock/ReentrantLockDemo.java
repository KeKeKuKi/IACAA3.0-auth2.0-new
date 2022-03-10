package com.gapache.commons.lock;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 线程有哪些状态？
 * synchronized 是可重入锁吗？是公平锁吗？实现原理？
 * Monitor对象和对象会关联起来。
 * 依赖于操作系统mutex 性能太差 1.6之前（需要调用操作系统）导致中断（状态切换 用户态切换为用户态）
 *
 * 1.6之后性能升级 偏向锁 ->（轻量级锁 底层也是基于cas做的）-> mutex 这里涉及到锁的升级膨胀过程
 * 对象头，实例数据，对齐填充。
 *
 * Mark Word + 元数据指针 数组长度（数组对象才有）
 *
 * jvm启动5s后才会开启偏向锁
 * 调用对象的hashcode方法会造成锁升级
 *
 * 偏向状态 锁状态标志
 *
 * aqs
 *
 * monitorenter
 * monitorexit
 *
 *  cas Unsafe类 compareAndSwap 硬件原语 cmpxchg 硬件原语支持的原子指令（安全的）
 *
 *  jmm
 *  synchronized和lock的区别，说下AQS
 *  Volatile关键字的底层原理
 *  说一下单例场景为什么要使用DCL
 *  CAS的底层原语原理
 *  unsafe有什么弊端
 *  说一下线程池的参数以及销毁过程
 *  线程池的线程数该如何设置
 *
 * 非公平锁：
 * @see java.util.concurrent.locks.ReentrantLock.NonfairSync#lock()
 * 1.当线程尝试获取锁的时候，会立即执行{@link java.util.concurrent.locks.AbstractQueuedSynchronizer#compareAndSetState(int, int)}进行cas操作来获取锁
 * 2.立即获取锁失败的时候再调用{@link java.util.concurrent.locks.AbstractQueuedSynchronizer#acquire(int)}方法
 * 3.AQS则会调用{@link java.util.concurrent.locks.ReentrantLock.NonfairSync#tryAcquire(int)}方法
 * 4.非公平的tryAcquire实际上则会调用{@link java.util.concurrent.locks.ReentrantLock.Sync#nonfairTryAcquire(int)}方法，
 *
 * 锁的状态字段为{@link java.util.concurrent.locks.AbstractQueuedSynchronizer#state}
 * 这个方法会先判断锁的状态是为0，是0的话则进行cas操作尝试加锁，如果cas加锁成功则将当前线程设置为锁的持有者并且返回true表示加锁成功。
 * 不是0的话则判断是否当前线程是否为锁的持有者，如果是锁的持有者则对锁的状态进行加上指定的值（这说明是可重入的），并且返回true表示加锁成功。
 *
 * 5.如果{@link java.util.concurrent.locks.ReentrantLock.NonfairSync#tryAcquire(int)}返回false，则将使用当前线程构造节点添加进队列中，并且使用
 * @see sun.misc.Unsafe#park(boolean, long) 使线程阻塞
 *
 * 6.当释放的时候#{@link java.util.concurrent.locks.ReentrantLock.Sync#tryRelease(int)}，将锁的状态字段的值更新为其减去指定的值后的状态。
 * 判断这时的锁的状态字段是否为0，如果是0的话则表示该线程持有的锁已全部释放，设置当前持有锁的线程为null。
 * 最后更新锁的状态并且返回锁是否已全部释放。
 *
 * 7.当全部释放以后{@link java.util.concurrent.locks.AbstractQueuedSynchronizer#release(int)}这个方法就会使用
 * @see sun.misc.Unsafe#unpark(Object)
 * 把最先进入队列的节点所表示的线程唤醒
 *
 * 公平锁：
 * @see java.util.concurrent.locks.ReentrantLock.FairSync#lock()
 * 1.当线程尝试获取锁的时候并不会立即去尝试获得锁，而是直接调用{@link java.util.concurrent.locks.AbstractQueuedSynchronizer#acquire(int)}方法
 * 2.这个方法会先去子类公平锁实现的{@link java.util.concurrent.locks.ReentrantLock.FairSync#tryAcquire(int)}方法
 * 3.这个方法也会判断锁的状态是为0，和非公锁的区别在于如果队列里有前任（也就是先添加进队列的线程节点），则不会立刻尝试获取锁。之后在后续中创建线程节点并加入队列中。
 * 4.然后锁的释放和可重入和非公平锁是一样的。
 *
 * 总结：公平锁可以确保获取到锁的顺序与申请获取锁的顺序保持一致，在非公平锁中当新的线程进来申请锁时，可以先抢到锁，可能造成先入队的线程饿死。
 *
 * 优缺点：
 *  非公平锁性能高于公平锁性能。首先，在恢复一个被挂起的线程与该线程真正运行之间存在着严重的延迟。而且，非公平锁能更充分的利用cpu的时间片，尽量的减少cpu空闲的状态时间。
 *
 * @author HuSen
 * @since 2020/7/23 1:46 下午
 */
public class ReentrantLockDemo {

    private static final boolean FAIR = true;

    private static final ThreadPoolExecutor THREAD_POOL_EXECUTOR;

    private static final int LOOP_TIMES = 100;

    static {
        THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(10, 10, 1000L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1000), r -> new Thread(r, "ReentrantLockDemo"), new ThreadPoolExecutor.AbortPolicy());
    }

    public static void main(String[] args) {
        final Lock used = new ReentrantLock(FAIR);
        for (int i = 0; i < LOOP_TIMES; i++) {
            THREAD_POOL_EXECUTOR.execute(() -> {
                // lock最好在try块之前并且紧贴try块调用
                used.lock();
                try {
                    System.out.println("当前线程:" + Thread.currentThread().getName() + "-" + Thread.currentThread().getId());
                } finally {
                    used.unlock();
                }
            });
        }

        while (THREAD_POOL_EXECUTOR.getActiveCount() > 0) {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        THREAD_POOL_EXECUTOR.shutdown();
    }
}
