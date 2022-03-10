package com.gapache.commons.jvm.bytecode.parse.attribute;

import lombok.Data;

/**
 * @author HuSen
 * create on 2020/3/28 00:05
 */
@Data
public class LineNumberTable {
    private Integer startPc;
    private Integer lineNumber;
}
