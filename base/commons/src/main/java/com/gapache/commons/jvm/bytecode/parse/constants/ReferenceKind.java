package com.gapache.commons.jvm.bytecode.parse.constants;

import lombok.Getter;

import java.util.Arrays;

/**
 * @author HuSen
 * create on 2020/3/30 11:22
 */
@Getter
public enum ReferenceKind {
    //
    REF_GET_FIELD(1, "getfield"),
    REF_GET_STATIC(2, "getstatic"),
    REF_PUT_FIELD(3, "putfield"),
    REF_PUT_STATIC(4, "putstatic"),
    REF_INVOKE_VIRTUAL(5, "invokevirtual"),
    REF_INVOKE_STATIC(6, "invokestaic"),
    REF_INVOKE_SPECIAL(7, "invokespecial"),
    REF_NEW_INVOKE_SPECIAL(8, "newinvokespecial"),
    REF_INVOKE_INTERFACE(9, "invokeinterface"),
    UNKNOWN(-1, "");

    private int kind;
    private String value;

    ReferenceKind(int kind, String value) {
        this.kind = kind;
        this.value = value;
    }

    public static ReferenceKind valueOf(int kind) {
        return Arrays
                .stream(ReferenceKind.values())
                .filter(referenceKind -> referenceKind.getKind() == kind)
                .findFirst()
                .orElse(UNKNOWN);

    }
}
