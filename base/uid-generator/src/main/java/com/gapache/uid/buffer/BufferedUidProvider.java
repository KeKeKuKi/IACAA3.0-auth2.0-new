package com.gapache.uid.buffer;

import java.util.List;

/**
 * @author HuSen
 * create on 2020/1/9 15:24
 */
@FunctionalInterface
public interface BufferedUidProvider {

    /**
     * Provides UID in one second
     *
     * @param momentInSecond 当前的秒数
     * @return UID in one second
     */
    List<Long> provide(long momentInSecond);
}
