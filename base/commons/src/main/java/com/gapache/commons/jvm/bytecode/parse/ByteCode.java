package com.gapache.commons.jvm.bytecode.parse;

import com.gapache.commons.jvm.bytecode.parse.attribute.AbstractAttributeInfo;
import com.gapache.commons.jvm.bytecode.parse.constants.AccessFlag;
import com.gapache.commons.jvm.bytecode.parse.cp.ConstantItem;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 我的字节码封装对象
 *
 * @author HuSen
 * create on 2020/3/28 00:00
 */
@Data
public class ByteCode {
    private String magicNumber;
    private Integer minorVersion;
    private Integer majorVersion;
    private Integer constantPoolCount;
    private Map<Integer, ConstantItem> constantPool;
    private List<AccessFlag> accessFlags;
    private Integer thisClass;
    private Integer superClass;
    private Integer interfacesCount;
    private List<Integer> interfaces;
    private Integer fieldsCount;
    private List<FieldInfo> fields;
    private Integer methodsCount;
    private List<MethodInfo> methods;
    private Integer attributesCount;
    private List<AbstractAttributeInfo> attributes;
}
