package com.gapache.commons.loader;

import java.util.List;

/**
 * @author HuSen
 * @since 2020/8/19 1:29 下午
 */
public interface DataLoader<T> {

    /**
     * 加载
     *
     * @return 加载到的数据
     */
    List<T> loading();
}
