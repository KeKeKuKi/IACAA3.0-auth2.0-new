package com.gapache.commons.jvm.bytecode.parse.func.impl;

import com.gapache.commons.jvm.bytecode.parse.Utils;
import com.gapache.commons.jvm.bytecode.parse.cp.IndexConstantItem;
import com.gapache.commons.jvm.bytecode.parse.cp.MethodHandleConstantItem;
import com.gapache.commons.jvm.bytecode.parse.cp.ValueConstantItem;
import com.gapache.commons.jvm.bytecode.parse.func.ConstantInfoParser;

import java.nio.charset.StandardCharsets;

/**
 * @author HuSen
 * create on 2020/3/28 00:28
 */
public final class ConstantInfoParserImpls {

    public static final ConstantInfoParser VALUE_CONSTANT_INFO_PARSE = (cpTag, point, context, byteCode) -> {
        String valueHex = context.substring(point, (point = point + cpTag.getBytesNumber() * 2));

        ValueConstantItem item = new ValueConstantItem();
        item.setTag(cpTag);
        Object value;
        switch (cpTag) {
            case CONSTANT_INTEGER_INFO:
                value = Utils.hexToInt(valueHex);
                break;
            case CONSTANT_FLOAT_INFO:
                value = Float.intBitsToFloat(Utils.hexToInt(valueHex));
                break;
            case CONSTANT_LONG_INFO:
                value = Utils.hexToLong(valueHex);
                break;
            case CONSTANT_DOUBLE_INFO:
                value = Double.longBitsToDouble(Utils.hexToLong(valueHex));
                break;
            case CONSTANT_UTF8_INFO: {
                int length = Utils.hexToInt(valueHex);
                String infoHex = context.substring(point, (point = point + length * 2));

                byte[] bytes = new byte[length];
                for (int i = 0; i < length; i++) {
                    String single = infoHex.substring(i * 2, i * 2 + 2);
                    bytes[i] = Integer.valueOf(Utils.hexToInt(single)).byteValue();
                }
                value = new String(bytes, StandardCharsets.UTF_8);
                break;
            }
            default:
                value = null;
        }
        item.setValue(value);
        byteCode.getConstantPool().put(byteCode.getConstantPool().size() + 1, item);
        return point;
    };

    public static final ConstantInfoParser INDEX_CONSTANT_INFO_PARSER = (cpTag, point, context, byteCode) -> {
        String valueHex = context.substring(point, (point = point + cpTag.getBytesNumber() * 2));
        int indexCount = valueHex.length() / 4;
        int[] indexes = new int[indexCount];
        for (int i = 0; i < indexCount; i++) {
            indexes[i] = Utils.hexToInt(valueHex.substring(i * 4, i * 4 + 4));
        }

        IndexConstantItem item = new IndexConstantItem();
        item.setTag(cpTag);
        item.setIndexes(indexes);
        byteCode.getConstantPool().put(byteCode.getConstantPool().size() + 1, item);
        return point;
    };

    public static final ConstantInfoParser METHOD_HANDLE_CONSTANT_INFO_PARSER = (cpTag, point, context, byteCode) -> {
        String valueHex = context.substring(point, (point = point + cpTag.getBytesNumber() * 2));
        String referenceKindHex = valueHex.substring(0, 2);
        String referenceIndexHex = valueHex.substring(2);

        MethodHandleConstantItem item = new MethodHandleConstantItem();
        item.setTag(cpTag);
        item.setReferenceKind(Utils.hexToInt(referenceKindHex));
        item.setReferenceIndex(Utils.hexToInt(referenceIndexHex));
        byteCode.getConstantPool().put(byteCode.getConstantPool().size() + 1, item);
        return point;
    };
}
