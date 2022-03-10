package com.gapache.commons.jvm.bytecode.parse.constants;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author HuSen
 * create on 2020/3/28 00:22
 */
@Getter
public enum AccessFlag {
    //
    ACC_PUBLIC(0x0001),
    ACC_PRIVATE(0x0002),
    ACC_PROTECTED(0x0004),
    ACC_STATIC(0x0008),
    ACC_FINAL(0x0010),
    ACC_SUPER(0x0020),
    ACC_VOLATILE(0x0040),
    ACC_TRANSIENT(0x0080),
    ACC_INTERFACE(0x0200),
    ACC_ABSTRACT(0x0400),
    ACC_SYNTHETIC(0x1000),
    ACC_ANNOTATION(0x2000),
    ACC_ENUM(0x4000);

    private int value;

    AccessFlag(int value) {
        this.value = value;
    }

    public static List<AccessFlag> checkAccessFlags(int accessFlags) {
        return Arrays.stream(AccessFlag.values())
                .filter(accessFlag -> (accessFlags & accessFlag.getValue()) == accessFlag.getValue())
                .collect(Collectors.toList());
    }
}
