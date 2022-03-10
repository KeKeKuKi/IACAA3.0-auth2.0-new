package com.gapache.redis;

import com.gapache.commons.utils.IStringUtils;

/**
 * @author HuSen
 * create on 2020/4/8 22:40
 */
public abstract class BaseDataStructure<ID> {

    /**
     * 返回在Redis中的key
     *
     * @param id ID
     * @return key
     */
    public String key(ID id) {
        return prefix() + id;
    }

    /**
     * 返回在Redis中的key
     *
     * @return key
     */
    public String key() {
        return prefix();
    }

    /**
     * 返回Redis中的key的前缀
     *
     * @return Redis中的key的前缀
     */
    protected abstract String prefix();

    /**
     * 返回键对应key的byte[]形式
     *
     * @param id ID
     * @return key的byte[]形式
     */
    public byte[] keyBytes(ID id) {
        return IStringUtils.getBytes(key(id));
    }

    /**
     * 返回键对应key的byte[]形式
     *
     * @return key的byte[]形式
     */
    public byte[] keyBytes() {
        return IStringUtils.getBytes(prefix());
    }
}
