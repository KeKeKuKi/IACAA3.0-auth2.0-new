package com.gapache.commons.jvm.bytecode.parse.func;

import com.gapache.commons.jvm.bytecode.parse.cp.ConstantItem;

import java.util.Map;

/**
 * @author HuSen
 * create on 2020/3/28 00:07
 */
@FunctionalInterface
public interface ConstantInfoPrinter {
    void print(Map<Integer, ConstantItem> constantPool, int index, ConstantItem item);
}
