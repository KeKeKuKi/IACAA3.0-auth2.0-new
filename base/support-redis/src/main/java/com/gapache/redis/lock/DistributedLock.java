package com.gapache.redis.lock;

import org.redisson.api.RFuture;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.lang.NonNull;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

/**
 * 分布式锁
 *
 * @author HuSen
 * @since 2021/1/28 2:49 下午
 */
public class DistributedLock implements RLock {

    private final RLock proxy;
    private static RedissonClient redissonClient;

    public static void setRedissonClient(RedissonClient redissonClient) {
        DistributedLock.redissonClient = redissonClient;
    }

    private DistributedLock(RLock proxy) {
        this.proxy = proxy;
    }

    public static DistributedLock getLock(String resource) {
        return new DistributedLock(redissonClient.getLock(resource));
    }

    public static DistributedLock getFairLock(String resource) {
        return new DistributedLock(redissonClient.getFairLock(resource));
    }

    @Override
    public String getName() {
        return proxy.getName();
    }

    @Override
    public void lockInterruptibly(long leaseTime, TimeUnit unit) throws InterruptedException {
        proxy.lockInterruptibly(leaseTime, unit);
    }

    @Override
    public boolean tryLock(long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException {
        return proxy.tryLock(waitTime, leaseTime, unit);
    }

    @Override
    public void lock(long leaseTime, TimeUnit unit) {
        proxy.lock(leaseTime, unit);
    }

    @Override
    public boolean forceUnlock() {
        return proxy.forceUnlock();
    }

    @Override
    public boolean isLocked() {
        return proxy.isLocked();
    }

    @Override
    public boolean isHeldByThread(long threadId) {
        return proxy.isHeldByThread(threadId);
    }

    @Override
    public boolean isHeldByCurrentThread() {
        return proxy.isHeldByCurrentThread();
    }

    @Override
    public int getHoldCount() {
        return proxy.getHoldCount();
    }

    @Override
    public long remainTimeToLive() {
        return proxy.remainTimeToLive();
    }

    @Override
    public void lock() {
        proxy.lock();
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        proxy.lockInterruptibly();
    }

    @Override
    public boolean tryLock() {
        return proxy.tryLock();
    }

    @Override
    public boolean tryLock(long time, @NonNull TimeUnit unit) throws InterruptedException {
        return proxy.tryLock(time, unit);
    }

    @Override
    public void unlock() {
        proxy.unlock();
    }

    @Override
    @NonNull
    public Condition newCondition() {
        return proxy.newCondition();
    }

    @Override
    public RFuture<Boolean> forceUnlockAsync() {
        return proxy.forceUnlockAsync();
    }

    @Override
    public RFuture<Void> unlockAsync() {
        return proxy.unlockAsync();
    }

    @Override
    public RFuture<Void> unlockAsync(long threadId) {
        return proxy.unlockAsync(threadId);
    }

    @Override
    public RFuture<Boolean> tryLockAsync() {
        return proxy.tryLockAsync();
    }

    @Override
    public RFuture<Void> lockAsync() {
        return proxy.lockAsync();
    }

    @Override
    public RFuture<Void> lockAsync(long threadId) {
        return proxy.lockAsync(threadId);
    }

    @Override
    public RFuture<Void> lockAsync(long leaseTime, TimeUnit unit) {
        return proxy.lockAsync(leaseTime, unit);
    }

    @Override
    public RFuture<Void> lockAsync(long leaseTime, TimeUnit unit, long threadId) {
        return proxy.lockAsync(leaseTime, unit, threadId);
    }

    @Override
    public RFuture<Boolean> tryLockAsync(long threadId) {
        return proxy.tryLockAsync(threadId);
    }

    @Override
    public RFuture<Boolean> tryLockAsync(long waitTime, TimeUnit unit) {
        return proxy.tryLockAsync(waitTime, unit);
    }

    @Override
    public RFuture<Boolean> tryLockAsync(long waitTime, long leaseTime, TimeUnit unit) {
        return proxy.tryLockAsync(waitTime, leaseTime, unit);
    }

    @Override
    public RFuture<Boolean> tryLockAsync(long waitTime, long leaseTime, TimeUnit unit, long threadId) {
        return proxy.tryLockAsync(waitTime, leaseTime, unit, threadId);
    }

    @Override
    public RFuture<Integer> getHoldCountAsync() {
        return proxy.getHoldCountAsync();
    }

    @Override
    public RFuture<Boolean> isLockedAsync() {
        return proxy.isLockedAsync();
    }

    @Override
    public RFuture<Long> remainTimeToLiveAsync() {
        return proxy.remainTimeToLiveAsync();
    }
}
