package com.gapache.commons.jvm.bytecode.parse.func.impl;

import com.gapache.commons.jvm.bytecode.parse.Utils;
import com.gapache.commons.jvm.bytecode.parse.constants.ReferenceKind;
import com.gapache.commons.jvm.bytecode.parse.cp.ConstantItem;
import com.gapache.commons.jvm.bytecode.parse.cp.IndexConstantItem;
import com.gapache.commons.jvm.bytecode.parse.cp.MethodHandleConstantItem;
import com.gapache.commons.jvm.bytecode.parse.cp.ValueConstantItem;
import com.gapache.commons.jvm.bytecode.parse.func.ConstantInfoPrinter;

/**
 * @author HuSen
 * create on 2020/3/28 00:42
 */
public final class ConstantInfoPrinterImpls {

    public static final ConstantInfoPrinter INVOKE_DYNAMIC_CONSTANT_INFO_PRINTER =
            (pool, index, item) ->
            {
                IndexConstantItem indexConstantItem = (IndexConstantItem) item;
                int bootstrapMethodIndex = indexConstantItem.getIndexes()[0];
                int nameAndTypeIndex = indexConstantItem.getIndexes()[1];

                IndexConstantItem nameAndType = (IndexConstantItem) pool.get(nameAndTypeIndex);
                int nameIndex = nameAndType.getIndexes()[0];
                int descriptorIndex = nameAndType.getIndexes()[1];
                ValueConstantItem name = (ValueConstantItem) pool.get(nameIndex);
                ValueConstantItem descriptor = (ValueConstantItem) pool.get(descriptorIndex);

                indexConstantItem.setDescription("// " + "#" + bootstrapMethodIndex + ":" + name.getValue() + ":" + descriptor.getValue());
                System.out.println(Utils.formatIndex(index) + " = " + indexConstantItem.getTag().getShowValue() +
                        Utils.formatValueOrIndex(indexConstantItem.getTag().getShowValue(), "#" + bootstrapMethodIndex + ":#" + nameAndTypeIndex) +
                        Utils.formatDescription("#" + bootstrapMethodIndex + ":#" + nameAndTypeIndex, indexConstantItem.getDescription()));
            };

    public static final ConstantInfoPrinter METHOD_HANDLE_CONSTANT_INFO_PRINTER =
            (pool, index, item) ->
            {
                MethodHandleConstantItem constantItem = (MethodHandleConstantItem) item;
                int referenceKind = constantItem.getReferenceKind();
                int referenceIndex = constantItem.getReferenceIndex();
                ReferenceKind kind = ReferenceKind.valueOf(referenceKind);

                IndexConstantItem reference = (IndexConstantItem) pool.get(referenceIndex);
                int classIndex = reference.getIndexes()[0];
                int nameAndTypeIndex = reference.getIndexes()[1];
                IndexConstantItem classInfo = (IndexConstantItem) pool.get(classIndex);
                ValueConstantItem className = (ValueConstantItem) pool.get(classInfo.getIndexes()[0]);

                IndexConstantItem nameAndType = (IndexConstantItem) pool.get(nameAndTypeIndex);
                int nameIndex = nameAndType.getIndexes()[0];
                int descriptorIndex = nameAndType.getIndexes()[1];
                ValueConstantItem name = (ValueConstantItem) pool.get(nameIndex);
                ValueConstantItem descriptor = (ValueConstantItem) pool.get(descriptorIndex);

                constantItem.setDescription("// " + kind.getValue() + " " + className.getValue() + "." + name.getValue() + ":" + descriptor.getValue());
                System.out.println(Utils.formatIndex(index) + " = " + constantItem.getTag().getShowValue() +
                        Utils.formatValueOrIndex(constantItem.getTag().getShowValue(), "#" + referenceKind + ":#" + referenceIndex) +
                        Utils.formatDescription("#" + referenceKind + ":#" + referenceIndex, constantItem.getDescription()));
            };

    public static final ConstantInfoPrinter VALUE_CONSTANT_INFO_PRINTER =
            (pool, index, item) ->
            {
                ValueConstantItem valueConstantItem = (ValueConstantItem) item;
                System.out.println(Utils.formatIndex(index) + " = " + item.getTag().getShowValue() +
                        Utils.formatValueOrIndex(item.getTag().getShowValue(), valueConstantItem.getValue().toString()));

            };

    public static final ConstantInfoPrinter CLASS_AND_STRING_CONSTANT_INFO_PRINTER =
            (pool, index, item) ->
            {
                IndexConstantItem indexItem = (IndexConstantItem) item;
                int utf8InfoIndex = indexItem.getIndexes()[0];
                ConstantItem utf8Info = pool.get(utf8InfoIndex);
                indexItem.setDescription("// " + ((ValueConstantItem) utf8Info).getValue());
                System.out.println(Utils.formatIndex(index) + " = " + indexItem.getTag().getShowValue() +
                        Utils.formatValueOrIndex(item.getTag().getShowValue(), "#" + utf8InfoIndex) +
                        Utils.formatDescription("#" + utf8InfoIndex, indexItem.getDescription()));
            };

    public static final ConstantInfoPrinter FIELD_AND_METHOD_REF_CONSTANT_INFO_PRINTER =
            (pool, index, item) ->
            {
                IndexConstantItem indexItem = (IndexConstantItem) item;
                int classIndex = indexItem.getIndexes()[0];
                int nameAndTypeIndex = indexItem.getIndexes()[1];
                IndexConstantItem classInfo = (IndexConstantItem) pool.get(classIndex);
                ValueConstantItem className = (ValueConstantItem) pool.get(classInfo.getIndexes()[0]);

                IndexConstantItem nameAndType = (IndexConstantItem) pool.get(nameAndTypeIndex);
                int nameIndex = nameAndType.getIndexes()[0];
                int descriptorIndex = nameAndType.getIndexes()[1];
                ValueConstantItem name = (ValueConstantItem) pool.get(nameIndex);
                ValueConstantItem descriptor = (ValueConstantItem) pool.get(descriptorIndex);

                indexItem.setDescription("// " + className.getValue() + "." + name.getValue() + ":" + descriptor.getValue());
                System.out.println(Utils.formatIndex(index) + " = " + indexItem.getTag().getShowValue() +
                        Utils.formatValueOrIndex(indexItem.getTag().getShowValue(), "#" + classIndex + ".#" + nameAndTypeIndex) +
                        Utils.formatDescription("#" + classIndex + ".#" + nameAndTypeIndex, indexItem.getDescription()));
            };

    public static final ConstantInfoPrinter NAME_AND_TYPE_CONSTANT_INFO_PRINTER =
            (pool, index, item) ->
            {
                IndexConstantItem indexItem = (IndexConstantItem) item;
                int nameIndex = indexItem.getIndexes()[0];
                int descriptorIndex = indexItem.getIndexes()[1];
                ValueConstantItem name = (ValueConstantItem) pool.get(nameIndex);
                ValueConstantItem type = (ValueConstantItem) pool.get(descriptorIndex);
                indexItem.setDescription("// " + name.getValue() + ":" + type.getValue());
                System.out.println(Utils.formatIndex(index) + " = " + indexItem.getTag().getShowValue() +
                        Utils.formatValueOrIndex(indexItem.getTag().getShowValue(), "#" + nameIndex + ":#" + descriptorIndex) +
                        Utils.formatDescription("#" + nameIndex + ":#" + descriptorIndex, indexItem.getDescription()));
            };
}
