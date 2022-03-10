package com.gapache.commons.jvm.bytecode.parse.attribute;

import lombok.Data;

import java.util.List;

/**
 * @author HuSen
 * create on 2020/3/28 00:06
 */
@Data
public class BootstrapMethod {
    private Integer bootstrapMethodRef;
    private Integer numBootstrapArguments;
    private List<String> bootstrapArguments;
}
