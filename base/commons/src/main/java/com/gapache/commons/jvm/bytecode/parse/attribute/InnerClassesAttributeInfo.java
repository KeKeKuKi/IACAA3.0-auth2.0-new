package com.gapache.commons.jvm.bytecode.parse.attribute;

import com.gapache.commons.jvm.bytecode.parse.AbstractTableView;
import com.gapache.commons.jvm.bytecode.parse.ByteCode;
import com.gapache.commons.jvm.bytecode.parse.Utils;
import com.gapache.commons.jvm.bytecode.parse.constants.AccessFlag;
import com.gapache.commons.jvm.bytecode.parse.cp.ConstantItem;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author HuSen
 * create on 2020/3/30 4:53 下午
 */
public class InnerClassesAttributeInfo extends AbstractTableView {

    private List<List<String>> trs;

    @Override
    public void parsing(ByteCode byteCode) {
        String hexString = super.getInfo().toString();
        int count = rows(hexString);
        int point = rowsHexLength() * 2;
        Map<Integer, ConstantItem> constantPool = byteCode.getConstantPool();
        trs = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            List<String> tr = new ArrayList<>(5);
            // 序号
            tr.add(String.valueOf(i));
            // innerClassIndex
            int innerClassIndex = Utils.hexToInt(hexString.substring(point, (point = point + 4)));
            String innerClassName = checkClassName(constantPool, innerClassIndex);
            tr.add("cp_info #" + innerClassIndex + " " + innerClassName);

            // outerClassIndex
            int outerClassIndex = Utils.hexToInt(hexString.substring(point, (point = point + 4)));
            String outerClassName = checkClassName(constantPool, outerClassIndex);
            tr.add("cp_info #" + outerClassIndex + " " + outerClassName);

            // innerNameIndex
            int innerNameIndex = Utils.hexToInt(hexString.substring(point, (point = point + 4)));
            String innerName = checkUtf8Value(constantPool, innerNameIndex);
            tr.add("cp_info #" + innerNameIndex + " " + innerName);

            // accessFlags
            String accessFlagsHex = hexString.substring(point, (point = point + 4));
            List<AccessFlag> accessFlags = AccessFlag.checkAccessFlags(Utils.hexToInt(accessFlagsHex));
            tr.add(accessFlagsHex + " [" + StringUtils.join(accessFlags, " ") + "]");
            trs.add(tr);
        }
    }

    @Override
    public void printing(ByteCode byteCode) {
        System.out.println("InnerClass:");
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
        return Arrays.asList("Nr.", "Inner Class", "Outer Class", "Inner Name", "Access Flags");
    }

    @Override
    public List<List<String>> trs() {
        return this.trs;
    }
}
