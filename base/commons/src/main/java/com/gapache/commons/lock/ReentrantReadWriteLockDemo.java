package com.gapache.commons.lock;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author HuSen
 * @since 2020/7/30 5:02 下午
 */
public class ReentrantReadWriteLockDemo {

    public static void main(String[] args) {
        ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    }
}
