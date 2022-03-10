package com.gapache.commons.jvm.bytecode.parse.func;

import com.gapache.commons.jvm.bytecode.parse.ByteCode;
import com.gapache.commons.jvm.bytecode.parse.constants.ByteCodeComposition;

/**
 * @author HuSen
 * create on 2020/3/28 00:00
 */
@FunctionalInterface
public interface ByteCodeCompositionParser {

    /**
     * 解析字节码对应的部分，并set对应的属性到字节码封装对象中
     *
     * @param composition 字节码组成
     * @param content     字节码总的16进制内容
     * @param point       解析时的读取开始位置
     * @param byteCode    字节码封装对象
     * @return 下次解析开始的位置
     */
    int parsing(ByteCodeComposition composition, String content, int point, ByteCode byteCode);
}
