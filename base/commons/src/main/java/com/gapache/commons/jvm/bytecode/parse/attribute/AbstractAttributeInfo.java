package com.gapache.commons.jvm.bytecode.parse.attribute;

import com.gapache.commons.jvm.bytecode.parse.ByteCode;
import com.gapache.commons.jvm.bytecode.parse.cp.ConstantItem;
import com.gapache.commons.jvm.bytecode.parse.cp.IndexConstantItem;
import com.gapache.commons.jvm.bytecode.parse.cp.ValueConstantItem;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author HuSen
 * create on 2020/3/28 00:03
 */
@Setter
@Getter
public abstract class AbstractAttributeInfo {
    private Integer attributeNameIndex;
    private Integer attributeLength;
    private Object info;

    public abstract void parsing(ByteCode byteCode);

    public abstract void printing(ByteCode byteCode);

    protected String checkClassName(Map<Integer, ConstantItem> constantPool, int index) {
        IndexConstantItem classInfo = (IndexConstantItem) constantPool.get(index);
        ValueConstantItem className = (ValueConstantItem) constantPool.get(classInfo.getIndexes()[0]);
        return className.getValue().toString();
    }

    protected String checkUtf8Value(Map<Integer, ConstantItem> constantPool, int index) {
        ValueConstantItem utf8Info = (ValueConstantItem) constantPool.get(index);
        return utf8Info.getValue().toString();
    }
}
