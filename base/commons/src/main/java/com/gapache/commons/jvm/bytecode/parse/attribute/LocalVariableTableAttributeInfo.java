package com.gapache.commons.jvm.bytecode.parse.attribute;

import com.gapache.commons.jvm.bytecode.parse.AbstractTableView;
import com.gapache.commons.jvm.bytecode.parse.ByteCode;
import com.gapache.commons.jvm.bytecode.parse.Utils;
import com.gapache.commons.jvm.bytecode.parse.cp.ConstantItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author HuSen
 * create on 2020/3/30 4:56 下午
 */
public class LocalVariableTableAttributeInfo extends AbstractTableView {

    private List<List<String>> trs;

    @Override
    public void parsing(ByteCode byteCode) {
        String hexString = super.getInfo().toString();
        int rows = super.rows(hexString);
        int point = 4;
        trs = new ArrayList<>(rows);
        Map<Integer, ConstantItem> constantPool = byteCode.getConstantPool();
        for (int i = 0; i < rows; i++) {
            List<String> tr = new ArrayList<>(6);
            tr.add(String.valueOf(i));

            int startPc = Utils.hexToInt(hexString.substring(point, (point = point + 4)));
            tr.add(String.valueOf(startPc));

            int length = Utils.hexToInt(hexString.substring(point, (point = point + 4)));
            tr.add(String.valueOf(length));

            int index = Utils.hexToInt(hexString.substring(point, (point = point + 4)));
            tr.add(String.valueOf(index));

            int nameIndex= Utils.hexToInt(hexString.substring(point, (point = point + 4)));
            String name = checkUtf8Value(constantPool, nameIndex);
            tr.add("cp_info #" + nameIndex + " " + name);

            int descriptorIndex = Utils.hexToInt(hexString.substring(point, (point = point + 4)));
            String descriptor = checkUtf8Value(constantPool, descriptorIndex);
            tr.add("cp_info #" + descriptorIndex + " " + descriptor);

            trs.add(tr);
        }
    }

    @Override
    public void printing(ByteCode byteCode) {
        System.out.println("LocalVariableTable:");
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
        return Arrays.asList("Nr.", "Start Pc", "Length", "Index", "Name", "Descriptor");
    }

    @Override
    public List<List<String>> trs() {
        return this.trs;
    }
}
