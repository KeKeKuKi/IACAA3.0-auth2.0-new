package com.gapache.commons.jvm.bytecode.parse.constants;

import com.gapache.commons.jvm.bytecode.parse.ByteCode;
import com.gapache.commons.jvm.bytecode.parse.func.ConstantInfoParser;
import com.gapache.commons.jvm.bytecode.parse.func.ConstantInfoPrinter;
import lombok.Getter;

import java.util.Arrays;

import static com.gapache.commons.jvm.bytecode.parse.func.impl.ConstantInfoParserImpls.*;
import static com.gapache.commons.jvm.bytecode.parse.func.impl.ConstantInfoPrinterImpls.*;

/**
 * @author HuSen
 * create on 2020/3/28 00:22
 */
@Getter
public enum CpTag {
    //
    CONSTANT_UTF8_INFO(1, 2, "Utf8", VALUE_CONSTANT_INFO_PARSE, VALUE_CONSTANT_INFO_PRINTER),
    CONSTANT_INTEGER_INFO(3, 4, "Integer", VALUE_CONSTANT_INFO_PARSE, VALUE_CONSTANT_INFO_PRINTER),
    CONSTANT_FLOAT_INFO(4, 4, "Float", VALUE_CONSTANT_INFO_PARSE, VALUE_CONSTANT_INFO_PRINTER),
    CONSTANT_LONG_INFO(5, 8, "Long", VALUE_CONSTANT_INFO_PARSE, VALUE_CONSTANT_INFO_PRINTER),
    CONSTANT_DOUBLE_INFO(6, 8, "Double", VALUE_CONSTANT_INFO_PARSE, VALUE_CONSTANT_INFO_PRINTER),
    CONSTANT_CLASS_INFO(7, 2, "Class", INDEX_CONSTANT_INFO_PARSER, CLASS_AND_STRING_CONSTANT_INFO_PRINTER),
    CONSTANT_STRING_INFO(8, 2, "String", INDEX_CONSTANT_INFO_PARSER, CLASS_AND_STRING_CONSTANT_INFO_PRINTER),
    CONSTANT_FILED_REF_INFO(9, 4, "Fieldref", INDEX_CONSTANT_INFO_PARSER, FIELD_AND_METHOD_REF_CONSTANT_INFO_PRINTER),
    CONSTANT_METHOD_REF_INFO(10, 4, "Methodref", INDEX_CONSTANT_INFO_PARSER, FIELD_AND_METHOD_REF_CONSTANT_INFO_PRINTER),
    CONSTANT_INTERFACE_METHOD_REF_INFO(11, 4, "InterfaceMethodref", INDEX_CONSTANT_INFO_PARSER, FIELD_AND_METHOD_REF_CONSTANT_INFO_PRINTER),
    CONSTANT_NAME_AND_TYPE_INFO(12, 4, "NameAndType", INDEX_CONSTANT_INFO_PARSER, NAME_AND_TYPE_CONSTANT_INFO_PRINTER),
    CONSTANT_METHOD_HANDLE_INFO(15, 3, "MethodHandle", METHOD_HANDLE_CONSTANT_INFO_PARSER, METHOD_HANDLE_CONSTANT_INFO_PRINTER),
    CONSTANT_METHOD_TYPE_INFO(16, 2, "MethodType", INDEX_CONSTANT_INFO_PARSER, CLASS_AND_STRING_CONSTANT_INFO_PRINTER),
    CONSTANT_INVOKE_DYNAMIC_INFO(18, 4, "InvokeDynamic", INDEX_CONSTANT_INFO_PARSER, INVOKE_DYNAMIC_CONSTANT_INFO_PRINTER),
    UNKNOWN(99, 0, "", null, null);

    private int value;
    private int bytesNumber;
    private String showValue;
    private ConstantInfoParser constantInfoParser;
    private ConstantInfoPrinter constantInfoPrinter;

    CpTag(int value, int bytesNumber, String showValue, ConstantInfoParser constantInfoParser, ConstantInfoPrinter constantInfoPrinter) {
        this.value = value;
        this.bytesNumber = bytesNumber;
        this.showValue = showValue;
        this.constantInfoParser = constantInfoParser;
        this.constantInfoPrinter = constantInfoPrinter;
    }

    public static CpTag valueOf(int value) {
        return Arrays.stream(CpTag.values())
                .filter(tag -> tag.getValue() == value)
                .findFirst()
                .orElse(CpTag.UNKNOWN);
    }

    public int parsing(int point, String context, ByteCode byteCode) {
        return this.constantInfoParser.parsing(this, point, context, byteCode);
    }
}
