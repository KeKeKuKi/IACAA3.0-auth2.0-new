package com.gapache.commons.jvm.classloader;

import com.sun.crypto.provider.AESKeyGenerator;

/**
 * @author HuSen
 * create on 2020/3/14 6:06 下午
 */
public class MyTest16 {

    public static void main(String[] args) {
        // 扩展类加载器
        AESKeyGenerator aesKeyGenerator = new AESKeyGenerator();

        System.out.println(aesKeyGenerator.getClass().getClassLoader());
        System.out.println(MyTest16.class.getClassLoader());
    }
}
