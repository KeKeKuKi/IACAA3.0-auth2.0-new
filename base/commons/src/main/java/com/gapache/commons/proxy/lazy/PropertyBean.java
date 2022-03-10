package com.gapache.commons.proxy.lazy;

import lombok.Getter;
import lombok.Setter;

/**
 * @author HuSen
 * @since 2020/5/27 1:57 下午
 */
@Getter
@Setter
public class PropertyBean {
    private String key;
    private Object value;

    @Override
    public String toString() {
        return "PropertyBean{" +
                "key='" + key + '\'' +
                ", value=" + value +
                '}' + getClass();
    }
}
