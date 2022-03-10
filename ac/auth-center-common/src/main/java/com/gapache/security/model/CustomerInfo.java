package com.gapache.security.model;

import java.util.HashMap;
import java.util.Set;

/**
 * @author HuSen
 * @since 2020/7/31 1:33 下午
 */
public class CustomerInfo extends HashMap<String, Object> {
    private static final long serialVersionUID = -4320942768666537636L;

    public CustomerInfo() {

    }

    public CustomerInfo(int size) {
        super(size);
    }

    public CustomerInfo copy() {
        CustomerInfo copy = new CustomerInfo(this.size());
        Set<Entry<String, Object>> entries = this.entrySet();
        for (Entry<String, Object> entry : entries) {
            copy.put(entry.getKey(), entry.getValue());
        }
        return copy;
    }
}
