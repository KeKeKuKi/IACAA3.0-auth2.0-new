package com.gapache.commons.jvm.bytecode.parse.attribute;

import com.gapache.commons.jvm.bytecode.parse.AbstractTableView;
import com.gapache.commons.jvm.bytecode.parse.ByteCode;
import com.gapache.commons.jvm.bytecode.parse.Utils;
import com.gapache.commons.jvm.bytecode.parse.cp.ConstantItem;
import com.gapache.commons.jvm.bytecode.parse.cp.IndexConstantItem;
import com.gapache.commons.jvm.bytecode.parse.cp.MethodHandleConstantItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author HuSen
 * create on 2020/3/30 4:53 下午
 */
public class BootstrapMethodsAttributeInfo extends AbstractTableView {

    private List<List<String>> trs;

    @Override
    public void parsing(ByteCode byteCode) {
        String hexString = super.getInfo().toString();
        int rows = rows(hexString);
        int point = 4;
        trs = new ArrayList<>(rows);
        Map<Integer, ConstantItem> constantPool = byteCode.getConstantPool();
        for (int i = 0; i < rows; i++) {
            List<String> tr = new ArrayList<>(3);
            tr.add(String.valueOf(i));

            int bootstrapMethodIndex = Utils.hexToInt(hexString.substring(point, (point = point + 4)));
            MethodHandleConstantItem bootstrapMethod = (MethodHandleConstantItem) constantPool.get(bootstrapMethodIndex);
            IndexConstantItem methodRef = (IndexConstantItem) constantPool.get(bootstrapMethod.getReferenceIndex());
            String className = checkClassName(constantPool, methodRef.getIndexes()[0]);
            IndexConstantItem nameAndType = (IndexConstantItem) constantPool.get(methodRef.getIndexes()[1]);
            String name = checkUtf8Value(constantPool, nameAndType.getIndexes()[0]);
            tr.add("cp_info  #" + bootstrapMethodIndex + " CONSTANT_MethodHandle_info " + className + "." + name);

            int argsCount = Utils.hexToInt(hexString.substring(point, (point = point + 4)));
            StringBuilder argsBuilder = new StringBuilder();
            for (int j = 0; j < argsCount; j++) {
                int methodTypeIndex = Utils.hexToInt(hexString.substring(point, (point = point + 4)));
                IndexConstantItem methodType = (IndexConstantItem) constantPool.get(methodTypeIndex);
                String type = checkUtf8Value(constantPool, methodType.getIndexes()[0]);
                argsBuilder.append("cp_info #").append(methodTypeIndex).append(" ").append(type).append(";");
            }
            tr.add(argsBuilder.toString());

            trs.add(tr);
        }
    }

    @Override
    public void printing(ByteCode byteCode) {
        System.out.println("BootstrapMethods:");
        System.out.println("Attribute length: " + super.getAttributeLength());
        System.out.println("Attribute Info:" + super.getInfo().toString());
        System.out.println("Specific info ___________________");
        super.draw();
    }

    @Override
    public int rowsHexLength() {
        return 2;
    }

    @Override
    public List<String> th() {
        return Arrays.asList("Nr.", "Bootstrap Method", "Arguments");
    }

    @Override
    public List<List<String>> trs() {
        return this.trs;
    }
}
