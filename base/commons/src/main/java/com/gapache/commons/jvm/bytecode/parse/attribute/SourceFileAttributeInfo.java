package com.gapache.commons.jvm.bytecode.parse.attribute;

import com.gapache.commons.jvm.bytecode.parse.ByteCode;
import com.gapache.commons.jvm.bytecode.parse.Utils;
import com.gapache.commons.jvm.bytecode.parse.cp.ConstantItem;
import lombok.Getter;
import lombok.Setter;

/**
 * @author HuSen
 * create on 2020/3/30 4:52 下午
 */
@Getter
@Setter
public class SourceFileAttributeInfo extends AbstractAttributeInfo {
    private Integer sourceFileNameIndex;
    private String description;

    @Override
    public void parsing(ByteCode byteCode) {
        String sourceFileNameIndexHex = super.getInfo().toString();
        this.sourceFileNameIndex = Utils.hexToInt(sourceFileNameIndexHex);

        ConstantItem constantItem = byteCode.getConstantPool().get(this.sourceFileNameIndex);
        Object value = constantItem.checkValue();
        this.description = value.toString();
    }

    @Override
    public void printing(ByteCode byteCode) {
        System.out.println("SourceFile: " + "\"" + this.description + "\"");
    }
}
