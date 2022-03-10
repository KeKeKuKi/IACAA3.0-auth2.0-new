package com.gapache.uid.utils;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Represents a padded {@link AtomicLong} to prevent the FalseSharing problem<p>
 *
 * The CPU cache line commonly be 64 bytes, here is a sample of cache line after padding:<br>
 * 64 bytes = 8 bytes (object reference) + 6 * 8 bytes (padded long) + 8 bytes (a long value)
 * @author HuSen
 * create on 2020/1/9 15:15
 */
public class PaddedAtomicLong extends AtomicLong {

    private static final long serialVersionUID = -8126716816882033900L;

    /**
     * Padded 6 long (48 bytes)
     */
    public volatile long p1, p2, p3, p4, p5, p6 = 7L;

    /**
     * Constructors from {@link AtomicLong}
     */
    public PaddedAtomicLong() {
        super();
    }

    public PaddedAtomicLong(long initialValue) {
        super(initialValue);
    }

    /**
     * To prevent GC optimizations for cleaning unused padded references
     * 为了防止GC优化以清除未使用的填充
     */
    public long sumPaddingToPreventOptimization() {
        return p1 + p2 + p3 + p4 + p5 + p6;
    }
}
