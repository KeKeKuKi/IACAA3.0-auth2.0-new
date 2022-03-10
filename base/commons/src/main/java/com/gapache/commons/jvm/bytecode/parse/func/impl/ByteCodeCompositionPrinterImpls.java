package com.gapache.commons.jvm.bytecode.parse.func.impl;

import com.gapache.commons.jvm.bytecode.parse.attribute.AbstractAttributeInfo;
import com.gapache.commons.jvm.bytecode.parse.constants.Attribute;
import com.gapache.commons.jvm.bytecode.parse.cp.ConstantItem;
import com.gapache.commons.jvm.bytecode.parse.cp.IndexConstantItem;
import com.gapache.commons.jvm.bytecode.parse.cp.ValueConstantItem;
import com.gapache.commons.jvm.bytecode.parse.func.ByteCodeCompositionPrinter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author HuSen
 * create on 2020/3/30 09:54
 */
public class ByteCodeCompositionPrinterImpls {

    public static final ByteCodeCompositionPrinter MAGIC_NUMBER_PRINTER =
            byteCode -> System.out.println("魔数: " + byteCode.getMagicNumber());

    public static final ByteCodeCompositionPrinter MINOR_VERSION_PRINTER =
            byteCode -> System.out.println("次版本号: " + byteCode.getMinorVersion());

    public static final ByteCodeCompositionPrinter MAJOR_VERSION_PRINTER =
            byteCode -> System.out.println("主版本号: " + byteCode.getMajorVersion());

    public static final ByteCodeCompositionPrinter CONSTANT_POOL_COUNT_PRINTER =
            byteCode -> System.out.println("常量池项数量: " + byteCode.getConstantPoolCount());

    public static final ByteCodeCompositionPrinter CONSTANT_POOL_PRINTER =
            byteCode ->
            {
                Map<Integer, ConstantItem> constantPool = byteCode.getConstantPool();
                System.out.println("常量池:");
                constantPool.forEach((index, constantItem) -> constantItem.getTag().getConstantInfoPrinter().print(constantPool, index, constantItem));
            };

    public static final ByteCodeCompositionPrinter ACCESS_FLAGS_PRINTER =
            byteCode -> System.out.println("访问修饰符: " + byteCode.getAccessFlags());

    public static final ByteCodeCompositionPrinter THIS_CLASS_PRINTER =
            byteCode ->
            {
                Integer thisClassIndex = byteCode.getThisClass();
                IndexConstantItem classInfo = (IndexConstantItem) byteCode.getConstantPool().get(thisClassIndex);
                System.out.println("当前类: " + checkValueFromConstantItem(byteCode.getConstantPool().get(classInfo.getIndexes()[0])));
            };

    public static final ByteCodeCompositionPrinter SUPER_CLASS_PRINTER =
            byteCode ->
            {
                Integer superClassIndex = byteCode.getSuperClass();
                IndexConstantItem classInfo = (IndexConstantItem) byteCode.getConstantPool().get(superClassIndex);
                System.out.println("父类: " + checkValueFromConstantItem(byteCode.getConstantPool().get(classInfo.getIndexes()[0])));
            };

    public static final ByteCodeCompositionPrinter INTERFACES_COUNT_PRINTER =
            byteCode -> System.out.println("接口数量: " + byteCode.getInterfacesCount());

    public static final ByteCodeCompositionPrinter INTERFACES_PRINTER =
            byteCode ->
            {
                List<String> interfaces = new ArrayList<>(byteCode.getInterfacesCount());
                for (Integer nameIndex : byteCode.getInterfaces()) {
                    ConstantItem classNameItem = byteCode.getConstantPool().get(nameIndex);
                    interfaces.add(checkValueFromConstantItem(classNameItem).toString());
                }
                System.out.println("接口: " + interfaces);
            };

    public static final ByteCodeCompositionPrinter FIELDS_COUNT_PRINTER =
            byteCode ->
            {
                System.out.println("------------------------------------------------------------");
                System.out.println("域数量: " + byteCode.getFieldsCount());
            };

    public static final ByteCodeCompositionPrinter FIELDS_PRINTER =
            byteCode ->
            {
                System.out.println("域列表:");
                byteCode.getFields()
                        .forEach(fieldInfo ->
                        {
                            System.out.println("访问修饰符: " + fieldInfo.getAccessFlags());
                            System.out.println("描述符: " + checkValueFromConstantItem(byteCode.getConstantPool().get(fieldInfo.getDescriptorIndex())));
                            System.out.println("名称: " + checkValueFromConstantItem(byteCode.getConstantPool().get(fieldInfo.getNameIndex())));
                            // TODO 属性
                            System.out.println("\r");
                        });
            };

    public static final ByteCodeCompositionPrinter METHODS_COUNT_PRINTER =
            byteCode ->
            {
                System.out.println("------------------------------------------------------------");
                System.out.println("方法数量: " + byteCode.getMethodsCount());
            };

    public static final ByteCodeCompositionPrinter METHODS_PRINTER =
            byteCode ->
            {
                System.out.println("方法列表:");
                byteCode.getMethods()
                        .forEach(methodInfo ->
                        {
                            System.out.println("访问修饰符: " + methodInfo.getAccessFlags());
                            System.out.println("描述符: " + checkValueFromConstantItem(byteCode.getConstantPool().get(methodInfo.getDescriptorIndex())));
                            System.out.println("名称: " + checkValueFromConstantItem(byteCode.getConstantPool().get(methodInfo.getNameIndex())));
                            System.out.println("属性数量: " + methodInfo.getAttributesCount());
                            for (AbstractAttributeInfo info : methodInfo.getAttributes()) {
                                String attributeName = checkValueFromConstantItem(byteCode.getConstantPool().get(info.getAttributeNameIndex())).toString();
                                Attribute attribute = Attribute.check(attributeName);
                                if (attribute != Attribute.UNKNOWN) {
                                    attribute.getPrinter().printing(byteCode, attributeName, info);
                                }
                                System.out.println("\r");
                            }
                            System.out.println("\r");
                        });
            };

    public static final ByteCodeCompositionPrinter ATTRIBUTES_COUNT_PRINTER =
            byteCode ->
            {
                System.out.println("------------------------------------------------------------");
                System.out.println("额外属性数量: " + byteCode.getAttributesCount());
            };

    public static final ByteCodeCompositionPrinter ATTRIBUTES_PRINTER =
            byteCode -> {
                System.out.println("额外属性列表:");
                byteCode.getAttributes()
                        .forEach(attributeInfo ->
                        {
                            String attributeName = checkValueFromConstantItem(byteCode.getConstantPool().get(attributeInfo.getAttributeNameIndex())).toString();
                            Attribute attribute = Attribute.check(attributeName);
                            if (attribute != Attribute.UNKNOWN) {
                                attribute.getPrinter().printing(byteCode, attributeName, attributeInfo);
                            }
                            System.out.println("\r");
                        });
            };


    public static Object checkValueFromConstantItem(ConstantItem constantItem) {
        return ((ValueConstantItem) constantItem).getValue();
    }
}
