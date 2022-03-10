package com.gapache.commons.function;

/**
 * @author HuSen
 * @since 2021/1/18 10:11 上午
 */
@FunctionalInterface
public interface ThreeConsumer<K, I, V> {

    /**
     * 接收参数并进行处理
     *
     * @param k 第一个参数
     * @param i 第二个参数
     * @param v 第三个参数
     */
    void accept(K k, I i, V v);
}
