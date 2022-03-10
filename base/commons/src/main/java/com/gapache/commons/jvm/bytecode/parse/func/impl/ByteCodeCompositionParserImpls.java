package com.gapache.commons.jvm.bytecode.parse.func.impl;

import com.gapache.commons.jvm.bytecode.parse.attribute.AbstractAttributeInfo;
import com.gapache.commons.jvm.bytecode.parse.FieldInfo;
import com.gapache.commons.jvm.bytecode.parse.MethodInfo;
import com.gapache.commons.jvm.bytecode.parse.Utils;
import com.gapache.commons.jvm.bytecode.parse.attribute.LineNumberTableAttributeInfo;
import com.gapache.commons.jvm.bytecode.parse.constants.AccessFlag;
import com.gapache.commons.jvm.bytecode.parse.constants.CpTag;
import com.gapache.commons.jvm.bytecode.parse.func.ByteCodeCompositionParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author HuSen
 * create on 2020/3/29 21:22
 */
public class ByteCodeCompositionParserImpls {

    public static final ByteCodeCompositionParser MAGIC_NUMBER_PARSER =
            (composition, content, point, byteCode) -> Utils.readData(content, composition.getLength() * 2, point, byteCode::setMagicNumber);

    public static final ByteCodeCompositionParser MINOR_VERSION_PARSER =
            (composition, content, point, byteCode) ->
                    Utils.readData(content, composition.getLength() * 2, point, minorVersionHex -> byteCode.setMinorVersion(Utils.hexToInt(minorVersionHex)));

    public static final ByteCodeCompositionParser MAJOR_VERSION_PARSER =
            (composition, content, point, byteCode) ->
                    Utils.readData(content, composition.getLength() * 2, point, majorVersionHex -> byteCode.setMajorVersion(Utils.hexToInt(majorVersionHex)));

    public static final ByteCodeCompositionParser CONSTANT_POOL_COUNT_PARSER =
            (composition, content, point, byteCode) ->
                    Utils.readData(content, composition.getLength() * 2, point, constantPoolCountHex -> byteCode.setConstantPoolCount(Utils.hexToInt(constantPoolCountHex)));

    public static final ByteCodeCompositionParser CONSTANT_POOL_PARSER =
            (composition, content, point, byteCode) ->
            {
                byteCode.setConstantPool(new HashMap<>(byteCode.getConstantPoolCount()));
                int constantPoolRealCount = byteCode.getConstantPoolCount() - 1;
                for (int i = 0; i < constantPoolRealCount; i++) {
                    String tagHex = content.substring(point, point + 2);
                    point += 2;
                    CpTag cpTag = CpTag.valueOf(Utils.hexToInt(tagHex));
                    point = cpTag.parsing(point, content, byteCode);
                }
                return point;
            };

    public static final ByteCodeCompositionParser ACCESS_FLAGS_PARSER =
            (composition, content, point, byteCode) -> Utils.readData(content, composition.getLength() * 2, point, accessFlagsHex -> {
                int accessFlags = Utils.hexToInt(accessFlagsHex);
                List<AccessFlag> flags = AccessFlag.checkAccessFlags(accessFlags);
                byteCode.setAccessFlags(flags);
            });

    public static final ByteCodeCompositionParser THIS_CLASS_PARSER =
            (composition, content, point, byteCode) ->
                    Utils.readData(content, composition.getLength() * 2, point, thisClassHex -> byteCode.setThisClass(Utils.hexToInt(thisClassHex)));

    public static final ByteCodeCompositionParser SUPER_CLASS_PARSER =
            (composition, content, point, byteCode) ->
                    Utils.readData(content, composition.getLength() * 2, point, superClassHex -> byteCode.setSuperClass(Utils.hexToInt(superClassHex)));

    public static final ByteCodeCompositionParser INTERFACES_COUNT_PARSER =
            (composition, content, point, byteCode) ->
                    Utils.readData(content, composition.getLength() * 2, point, interfacesCountHex -> byteCode.setInterfacesCount(Utils.hexToInt(interfacesCountHex)));

    public static final ByteCodeCompositionParser INTERFACES_PARSER =
            (composition, content, point, byteCode) ->
            {
                byteCode.setInterfaces(new ArrayList<>(byteCode.getInterfacesCount()));
                for (int i = 0; i < byteCode.getInterfacesCount(); i++) {
                    point = Utils
                            .readData(content, composition.getLength() * 2, point, interfaceIndexHex -> byteCode.getInterfaces()
                                    .add(Utils.hexToInt(interfaceIndexHex)));
                }
                return point;
            };

    public static final ByteCodeCompositionParser FIELDS_COUNT_PARSER =
            (composition, content, point, byteCode) ->
                    Utils.readData(content, composition.getLength() * 2, point, fieldsCountHex -> byteCode.setFieldsCount(Utils.hexToInt(fieldsCountHex)));

    public static final ByteCodeCompositionParser FIELDS_PARSER =
            (composition, content, point, byteCode) ->
            {
                List<FieldInfo> fields = new ArrayList<>(byteCode.getFieldsCount());
                for (int i = 0; i < byteCode.getFieldsCount(); i++) {
                    FieldInfo fieldInfo = new FieldInfo();
                    String accessFlagsHex = content.substring(point, (point = point + 4));
                    fieldInfo.setAccessFlags(AccessFlag.checkAccessFlags(Utils.hexToInt(accessFlagsHex)));

                    String nameIndexHex = content.substring(point, (point = point + 4));
                    fieldInfo.setNameIndex(Utils.hexToInt(nameIndexHex));

                    String descriptorIndexHex = content.substring(point, (point = point + 4));
                    fieldInfo.setDescriptorIndex(Utils.hexToInt(descriptorIndexHex));

                    String attributesCountHex = content.substring(point, (point = point + 4));
                    fieldInfo.setAttributesCount(Utils.hexToInt(attributesCountHex));

                    List<AbstractAttributeInfo> attributes = new ArrayList<>(fieldInfo.getAttributesCount());
                    for (int j = 0; j < fieldInfo.getAttributesCount(); j++) {
                        AbstractAttributeInfo attributeInfo = new LineNumberTableAttributeInfo();
                        String attributeNameIndexHex = content.substring(point, (point = point + 4));
                        attributeInfo.setAttributeNameIndex(Utils.hexToInt(attributeNameIndexHex));

                        String attributeLengthHex = content.substring(point, (point = point + 8));
                        attributeInfo.setAttributeLength(Utils.hexToInt(attributeLengthHex));

                        String infoHex = content.substring(point, (point = point + attributeInfo.getAttributeLength() * 2));
                        attributeInfo.setInfo(infoHex);

                        attributes.add(attributeInfo);
                    }
                    fieldInfo.setAttributes(attributes);

                    fields.add(fieldInfo);
                }
                byteCode.setFields(fields);
                return point;
            };

    public static final ByteCodeCompositionParser METHODS_COUNT_PARSER =
            (composition, content, point, byteCode) ->
                    Utils.readData(content, composition.getLength() * 2, point, methodsCountHex -> byteCode.setMethodsCount(Utils.hexToInt(methodsCountHex)));

    public static final ByteCodeCompositionParser METHODS_PARSER =
            (composition, content, point, byteCode) ->
            {
                List<MethodInfo> methods = new ArrayList<>(byteCode.getMethodsCount());
                for (int i = 0; i < byteCode.getMethodsCount(); i++) {
                    MethodInfo methodInfo = new MethodInfo();
                    String accessFlagsHex = content.substring(point, (point = point + 4));
                    methodInfo.setAccessFlags(AccessFlag.checkAccessFlags(Utils.hexToInt(accessFlagsHex)));

                    String nameIndexHex = content.substring(point, (point = point + 4));
                    methodInfo.setNameIndex(Utils.hexToInt(nameIndexHex));

                    String descriptorIndexHex = content.substring(point, (point = point + 4));
                    methodInfo.setDescriptorIndex(Utils.hexToInt(descriptorIndexHex));

                    String attributesCountHex = content.substring(point, (point = point + 4));
                    methodInfo.setAttributesCount(Utils.hexToInt(attributesCountHex));

                    List<AbstractAttributeInfo> attributes = new ArrayList<>(methodInfo.getAttributesCount());
                    for (int j = 0; j < methodInfo.getAttributesCount(); j++) {
                        AbstractAttributeInfo attributeInfo = new LineNumberTableAttributeInfo();
                        String attributeNameIndexHex = content.substring(point, (point = point + 4));
                        attributeInfo.setAttributeNameIndex(Utils.hexToInt(attributeNameIndexHex));

                        String attributeLengthHex = content.substring(point, (point = point + 8));
                        attributeInfo.setAttributeLength(Utils.hexToInt(attributeLengthHex));

                        String infoHex = content.substring(point, (point = point + attributeInfo.getAttributeLength() * 2));
                        attributeInfo.setInfo(infoHex);

                        attributes.add(attributeInfo);
                    }
                    methodInfo.setAttributes(attributes);

                    methods.add(methodInfo);
                }
                byteCode.setMethods(methods);
                return point;
            };

    public static final ByteCodeCompositionParser ATTRIBUTES_COUNT_PARSER =
            (composition, content, point, byteCode) ->
                    Utils.readData(content, composition.getLength() * 2, point, attributesCountHex -> byteCode.setAttributesCount(Utils.hexToInt(attributesCountHex)));

    public static final ByteCodeCompositionParser ATTRIBUTES_PARSER =
            (composition, content, point, byteCode) ->
            {
                List<AbstractAttributeInfo> attributes = new ArrayList<>(byteCode.getAttributesCount());
                for (int i = 0; i < byteCode.getAttributesCount(); i++) {
                    AbstractAttributeInfo attributeInfo = new LineNumberTableAttributeInfo();
                    String attributeNameIndexHex = content.substring(point, (point = point + 4));
                    attributeInfo.setAttributeNameIndex(Utils.hexToInt(attributeNameIndexHex));

                    String attributeLengthHex = content.substring(point, (point = point + 8));
                    attributeInfo.setAttributeLength(Utils.hexToInt(attributeLengthHex));

                    String infoHex = content.substring(point, (point = point + attributeInfo.getAttributeLength() * 2));
                    attributeInfo.setInfo(infoHex);

                    attributes.add(attributeInfo);
                }
                byteCode.setAttributes(attributes);
                return point;
            };
}
