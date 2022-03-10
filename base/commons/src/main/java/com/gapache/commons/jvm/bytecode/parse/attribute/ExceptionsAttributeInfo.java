package com.gapache.commons.jvm.bytecode.parse.attribute;

import com.gapache.commons.jvm.bytecode.parse.AbstractTableView;
import com.gapache.commons.jvm.bytecode.parse.ByteCode;

import java.util.List;

/**
 * @author HuSen
 * create on 2020/4/2 11:29 上午
 */
public class ExceptionsAttributeInfo extends AbstractTableView {

    @Override
    public int rowsHexLength() {
        return 0;
    }

    @Override
    public List<String> th() {
        return null;
    }

    @Override
    public List<List<String>> trs() {
        return null;
    }

    @Override
    public void parsing(ByteCode byteCode) {

    }

    @Override
    public void printing(ByteCode byteCode) {

    }
}
