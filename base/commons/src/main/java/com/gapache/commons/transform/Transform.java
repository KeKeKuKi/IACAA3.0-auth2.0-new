package com.gapache.commons.transform;

/**
 * 模型转换接口
 *
 * @author HuSen
 * @since 2021/1/15 11:49 上午
 */
public interface Transform<T, R> {

    /**
     * R转T
     *
     * @param r R
     * @return t
     */
    T toT(R r);

    /**
     * T转R
     *
     * @param t T
     * @return r
     */
    R toR(T t);

}
