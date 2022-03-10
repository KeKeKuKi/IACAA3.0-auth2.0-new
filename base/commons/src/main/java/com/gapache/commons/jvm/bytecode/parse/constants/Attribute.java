package com.gapache.commons.jvm.bytecode.parse.constants;

import com.gapache.commons.jvm.bytecode.parse.func.AttributePrinter;
import lombok.Getter;

import java.util.Arrays;

import static com.gapache.commons.jvm.bytecode.parse.func.impl.AttributePrinterImpls.*;

/**
 * @author HuSen
 * create on 2020/3/30 2:07 下午
 */
@Getter
public enum Attribute {
    //
    SOURCE_FILE("SourceFile", SOURCE_FILE_PRINTER),
    INNER_CLASSES("InnerClasses", INNER_CLASSES_PRINTER),
    BOOTSTRAP_METHODS("BootstrapMethods", BOOTSTRAP_METHODS_PRINTER),
    CODE("Code", CODE_PRINTER),
    LINE_NUMBER_TABLE("LineNumberTable", LINE_NUMBER_TABLE_PRINTER),
    LOCAL_VARIABLE_TABLE("LocalVariableTable", LOCAL_VARIABLE_TABLE_PRINTER),
    METHOD_PARAMETERS("MethodParameters", METHOD_PARAMETERS_PRINTER),
    UNKNOWN("", null);
    private String value;
    private AttributePrinter printer;

    Attribute(String value, AttributePrinter printer) {
        this.value = value;
        this.printer = printer;
    }

    public static Attribute check(String value) {
        return Arrays
                .stream(values())
                .filter(attribute -> attribute.value.equals(value))
                .findFirst()
                .orElse(UNKNOWN);
    }
}
