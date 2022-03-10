package com.gapache.commons.jvm.bytecode.parse.cp;

import lombok.Getter;
import lombok.Setter;

/**
 * @author HuSen
 * create on 2020/3/28 00:33
 */
@Getter
@Setter
public class ValueConstantItem extends ConstantItem {
    private Object value;
}
