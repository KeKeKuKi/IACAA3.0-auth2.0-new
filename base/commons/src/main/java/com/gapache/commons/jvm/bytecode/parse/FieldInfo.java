package com.gapache.commons.jvm.bytecode.parse;

import com.gapache.commons.jvm.bytecode.parse.attribute.AbstractAttributeInfo;
import com.gapache.commons.jvm.bytecode.parse.constants.AccessFlag;
import lombok.Data;

import java.util.List;

/**
 * 我的字节码解析工具
 *
 * @author HuSen
 * create on 2020/3/28 00:02
 */
@Data
public class FieldInfo {
    private List<AccessFlag> accessFlags;
    private Integer nameIndex;
    private Integer descriptorIndex;
    private Integer attributesCount;
    private List<AbstractAttributeInfo> attributes;
}
