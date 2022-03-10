package com.gapache.jpa.primitive;

/**
 * sql加载接口，由子类实现
 *
 * @author HuSen
 * @since 2021/1/29 10:28 上午
 */
public interface SqlLoader {
    
    /**
     * 加载sql语句
     *
     * @return sql语句
     */
    String loading();

    /**
     * 是否已加载
     *
     * @return 是否已加载
     */
    boolean loaded();
}
