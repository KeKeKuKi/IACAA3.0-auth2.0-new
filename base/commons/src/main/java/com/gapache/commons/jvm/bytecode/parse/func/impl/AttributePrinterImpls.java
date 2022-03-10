package com.gapache.commons.jvm.bytecode.parse.func.impl;

import com.gapache.commons.jvm.bytecode.parse.attribute.AbstractAttributeInfo;
import com.gapache.commons.jvm.bytecode.parse.attribute.CodeAttributeInfo;
import com.gapache.commons.jvm.bytecode.parse.Utils;
import com.gapache.commons.jvm.bytecode.parse.constants.AccessFlag;
import com.gapache.commons.jvm.bytecode.parse.constants.Attribute;
import com.gapache.commons.jvm.bytecode.parse.cp.ValueConstantItem;
import com.gapache.commons.jvm.bytecode.parse.func.AttributePrinter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author HuSen
 * create on 2020/3/30 2:10 下午
 */
public class AttributePrinterImpls {

    public static final AttributePrinter SOURCE_FILE_PRINTER =
            (byteCode, attributeName, info) ->
            {
                int sourceFileIndex = Utils.hexToInt(info.getInfo().toString());
                Object value = ((ValueConstantItem) byteCode.getConstantPool().get(sourceFileIndex)).getValue();
                System.out.println(attributeName + ": " + "\"" + value + "\"");
            };

    public static final AttributePrinter INNER_CLASSES_PRINTER =
            (byteCode, attributeName, info) ->
            {
                String hexString = info.getInfo().toString();
                int point = 0;
                String countHex = hexString.substring(point, (point = point + 4));
                int count = Utils.hexToInt(countHex);
                for (int i = 0; i < count; i++) {
                    System.out.println(i);
                    String innerClassIndex = hexString.substring(point, (point = point + 4));
                    System.out.println(Utils.hexToInt(innerClassIndex));
                    String outerClassIndex = hexString.substring(point, (point = point + 4));
                    System.out.println(Utils.hexToInt(outerClassIndex));
                    String innerNameIndex = hexString.substring(point, (point = point + 4));
                    System.out.println(Utils.hexToInt(innerNameIndex));
                    String accessFlagsHex = hexString.substring(point, (point = point + 4));
                    System.out.println(AccessFlag.checkAccessFlags(Utils.hexToInt(accessFlagsHex)));
                    System.out.println("\r");
                }
            };

    public static final AttributePrinter BOOTSTRAP_METHODS_PRINTER =
            (byteCode, attributeName, info) ->
            {
                String hexString = info.getInfo().toString();
                int point = 0;
                String countHex = hexString.substring(point, (point = point + 4));
                int count = Utils.hexToInt(countHex);
                for (int i = 0; i < count; i++) {
                    System.out.println(i);
                    String bootstrapMethodIndex = hexString.substring(point, (point = point + 4));
                    System.out.println(Utils.hexToInt(bootstrapMethodIndex));
                    String argsCountHex = hexString.substring(point, (point = point + 4));
                    int argsCount = Utils.hexToInt(argsCountHex);
                    System.out.println(argsCount);
                    for (int j = 0; j < argsCount; j++) {
                        System.out.println(Utils.hexToInt(hexString.substring(point, (point = point + 4))));
                    }
                    System.out.println("\r");
                }
            };

    public static final AttributePrinter CODE_PRINTER =
            (byteCode, attributeName, info) ->
            {
                CodeAttributeInfo codeAttribute = new CodeAttributeInfo();
                codeAttribute.setAttributeNameIndex(info.getAttributeNameIndex());
                codeAttribute.setAttributeLength(info.getAttributeLength());
                String hexString = info.getInfo().toString();
                int point = 0;
                String maxStacksHex = hexString.substring(point, (point = point + 4));
                System.out.println(Utils.hexToInt(maxStacksHex));

                String maxLocalsHex = hexString.substring(point, (point = point + 4));
                System.out.println(Utils.hexToInt(maxLocalsHex));

                String codeLengthHex = hexString.substring(point, (point = point + 8));
                System.out.println(Utils.hexToInt(codeLengthHex));

                String codeHex = hexString.substring(point, (point = point + Utils.hexToInt(codeLengthHex) * 2));
                System.out.println(codeHex);

                String exceptionTableLengthHex = hexString.substring(point, (point = point + 4));
                System.out.println(Utils.hexToInt(exceptionTableLengthHex));

                String exceptionTablesHex = hexString.substring(point, (point + Utils.hexToInt(exceptionTableLengthHex) * 2));
                System.out.println(exceptionTablesHex);

                String attributesCountHex = hexString.substring(point, (point = point + 4));
                int attributesCount = Utils.hexToInt(attributesCountHex);
                System.out.println(attributesCount);

                String attributesHex = hexString.substring(point);
                List<AbstractAttributeInfo> attributeInfos = new ArrayList<>(attributesCount);

                int attrPoint = 0;
                for (int i = 0; i < attributesCount; i++) {
                    AbstractAttributeInfo attributeInfo = new CodeAttributeInfo();
                    String attributeNameIndexHex = attributesHex.substring(attrPoint, (attrPoint = attrPoint + 4));
                    attributeInfo.setAttributeNameIndex(Utils.hexToInt(attributeNameIndexHex));

                    String attributeLengthHex = attributesHex.substring(attrPoint, (attrPoint = attrPoint + 8));
                    attributeInfo.setAttributeLength(Utils.hexToInt(attributeLengthHex));

                    String infoHex = attributesHex.substring(attrPoint, (attrPoint = attrPoint + attributeInfo.getAttributeLength() * 2));
                    attributeInfo.setInfo(infoHex);

                    attributeInfos.add(attributeInfo);
                }

                attributeInfos.forEach(a -> {
                    String name = ((ValueConstantItem) byteCode.getConstantPool().get(a.getAttributeNameIndex())).getValue().toString();
                    Attribute attribute = Attribute.check(name);
                    if (attribute != Attribute.UNKNOWN) {
                        attribute.getPrinter().printing(byteCode, name, a);
                    }
                    System.out.println("\r");
                });
                System.out.println("\r");
            };

    public static final AttributePrinter LINE_NUMBER_TABLE_PRINTER =
            (byteCode, attributeName, info) ->
            {
                System.out.println(attributeName);
                String hexString = info.getInfo().toString();
                int point = 0;
                String rowsHex = hexString.substring(point, (point = point + 4));
                int rows = Utils.hexToInt(rowsHex);
                System.out.println(rows);

                for (int i = 0; i < rows; i++) {
                    String startPcHex = hexString.substring(point, (point = point + 4));
                    System.out.println(Utils.hexToInt(startPcHex));
                    String lineNumber = hexString.substring(point, (point = point + 4));
                    System.out.println(Utils.hexToInt(lineNumber));
                }
            };

    public static final AttributePrinter LOCAL_VARIABLE_TABLE_PRINTER =
            (byteCode, attributeName, info) ->
            {
                System.out.println(attributeName);
                String hexString = info.getInfo().toString();
                int point = 0;
                String rowsHex = hexString.substring(point, (point = point + 4));
                int rows = Utils.hexToInt(rowsHex);
                System.out.println(rows);
                for (int i = 0; i < rows; i++) {
                    String startPcHex = hexString.substring(point, (point = point + 4));
                    System.out.println(Utils.hexToInt(startPcHex));
                    String lengthHex = hexString.substring(point, (point = point + 4));
                    System.out.println(Utils.hexToInt(lengthHex));
                    String indexHex = hexString.substring(point, (point = point + 4));
                    System.out.println(Utils.hexToInt(indexHex));
                    String nameIndexHex = hexString.substring(point, (point = point + 4));
                    System.out.println(Utils.hexToInt(nameIndexHex));
                    String descriptorIndexHex = hexString.substring(point, (point = point + 4));
                    System.out.println(Utils.hexToInt(descriptorIndexHex));
                }
            };

    public static final AttributePrinter METHOD_PARAMETERS_PRINTER =
            (byteCode, attributeName, info) ->
            {
                System.out.println(attributeName);
                String hexString = info.getInfo().toString();
                int point = 0;
                String rowsHex = hexString.substring(point, (point = point + 2));
                int rows = Utils.hexToInt(rowsHex);
                System.out.println(rows);
                for (int i = 0; i < rows; i++) {
                    String parameterNameIndexHex = hexString.substring(point, (point = point + 4));
                    System.out.println(Utils.hexToInt(parameterNameIndexHex));
                    String accessFlagsHex = hexString.substring(point, (point = point + 4));
                    System.out.println(Utils.hexToInt(accessFlagsHex));
                }
            };
}
