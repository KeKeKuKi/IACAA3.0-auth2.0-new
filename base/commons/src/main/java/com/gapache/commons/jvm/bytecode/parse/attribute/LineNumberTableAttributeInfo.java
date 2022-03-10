package com.gapache.commons.jvm.bytecode.parse.attribute;

import com.gapache.commons.jvm.bytecode.parse.AbstractTableView;
import com.gapache.commons.jvm.bytecode.parse.ByteCode;
import com.gapache.commons.jvm.bytecode.parse.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author HuSen
 * create on 2020/3/30 4:55 下午
 */
public class LineNumberTableAttributeInfo extends AbstractTableView {

    private List<List<String>> trs;

    @Override
    public void parsing(ByteCode byteCode) {
        String hexString = super.getInfo().toString();
        int rows = super.rows(hexString);
        int point = 4;
        trs = new ArrayList<>(rows);
        for (int i = 0; i < rows; i++) {
            List<String> tr = new ArrayList<>(3);
            tr.add(String.valueOf(i));

            int startPc = Utils.hexToInt(hexString.substring(point, (point = point + 4)));
            tr.add(String.valueOf(startPc));

            int lineNumber = Utils.hexToInt(hexString.substring(point, (point = point + 4)));
            tr.add(String.valueOf(lineNumber));

            trs.add(tr);
        }
    }

    @Override
    public void printing(ByteCode byteCode) {
        System.out.println("LineNumberTable:");
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
        return Arrays.asList("Nr.", "Start PC", "Line Number");
    }

    @Override
    public List<List<String>> trs() {
        return this.trs;
    }
}
