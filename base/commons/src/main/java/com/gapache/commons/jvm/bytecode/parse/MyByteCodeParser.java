package com.gapache.commons.jvm.bytecode.parse;

import com.gapache.commons.jvm.bytecode.parse.constants.ByteCodeComposition;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigInteger;

/**
 * 我的字节码解析工具
 *
 * @author HuSen
 * create on 2020/3/24 6:06 下午
 */
public class MyByteCodeParser {

    private String filePath;
    private int point;
    private ByteCode byteCode;

    public MyByteCodeParser(String filePath) {
        this.filePath = filePath;
        this.byteCode = new ByteCode();
    }

    public void parse() {
        byte[] data;
        try (InputStream is = new FileInputStream(new File(this.filePath)); ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            int ch;
            while (-1 != (ch = is.read())) {
                byteArrayOutputStream.write(ch);
            }
            data = byteArrayOutputStream.toByteArray();
            String content = new BigInteger(1, data).toString(16);
            for (ByteCodeComposition composition : ByteCodeComposition.values()) {
                point = composition.parsing(content, this.point, this.byteCode);
            }

            for (ByteCodeComposition composition : ByteCodeComposition.values()) {
                composition.getPrinter().printing(this.byteCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new MyByteCodeParser("/Users/macos/Documents/codes/mine/big-plan/base/commons/target/classes/com/gapache/commons/jvm/bytecode/parse/func/impl/ByteCodeCompositionParserImpls.class").parse();
    }
}
