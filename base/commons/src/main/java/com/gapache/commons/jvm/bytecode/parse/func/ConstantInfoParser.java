package com.gapache.commons.jvm.bytecode.parse.func;

import com.gapache.commons.jvm.bytecode.parse.ByteCode;
import com.gapache.commons.jvm.bytecode.parse.constants.CpTag;

/**
 * @author HuSen
 * create on 2020/3/28 00:07
 */
public interface ConstantInfoParser {
    int parsing(CpTag cpTag, int point, String context, ByteCode byteCode);
}
