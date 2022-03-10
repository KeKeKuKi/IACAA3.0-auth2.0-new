package com.gapache.commons.jvm.bytecode.parse.attribute;

import com.gapache.commons.jvm.bytecode.parse.AbstractTableView;
import com.gapache.commons.jvm.bytecode.parse.ByteCode;
import com.gapache.commons.jvm.bytecode.parse.Utils;
import com.gapache.commons.jvm.bytecode.parse.constants.AccessFlag;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author HuSen
 * create on 2020/3/30 4:56 下午
 */
public class MethodParametersAttributeInfo extends AbstractTableView {

    private List<List<String>> trs;

    @Override
    public void parsing(ByteCode byteCode) {
        String hexString = super.getInfo().toString();
        int rows = super.rows(hexString);
        trs = new ArrayList<>(rows);
        int point = 2;
        for (int i = 0; i < rows; i++) {
            List<String> tr = new ArrayList<>(3);
            tr.add(String.valueOf(i));

            int nameIndex = Utils.hexToInt(hexString.substring(point, (point + 4)));
            String name = checkUtf8Value(byteCode.getConstantPool(), nameIndex);
            tr.add("cp_info #" + nameIndex + " " + name);

            int accessFlags = Utils.hexToInt(hexString.substring(point, (point = point + 4)));
            List<AccessFlag> flags = AccessFlag.checkAccessFlags(accessFlags);
            tr.add("[" + StringUtils.join(flags, " ") + "]");

            trs.add(tr);
        }
    }

    @Override
    public void printing(ByteCode byteCode) {
        System.out.println("MethodParameters:");
        System.out.println("Attribute length: " + super.getAttributeLength());
        System.out.println("Attribute Info:" + super.getInfo().toString());
        System.out.println("Specific info ___________________");
        super.draw();
    }

    @Override
    public int rowsHexLength() {
        return 1;
    }

    @Override
    public List<String> th() {
        return Arrays.asList("Nr.", "Parameter Name", "Access Flags");
    }

    @Override
    public List<List<String>> trs() {
        return this.trs;
    }
}
