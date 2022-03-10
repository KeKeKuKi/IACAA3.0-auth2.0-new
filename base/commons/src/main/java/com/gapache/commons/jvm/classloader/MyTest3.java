package com.gapache.commons.jvm.classloader;

import java.util.UUID;

/**
 * @author HuSen
 * create on 2020/1/19
 */
public class MyTest3 {

    public static void main(String[] args) {
        // 当一个常量的值并非编译期间可以确定的，那么其值就不会被放到调用类的常量池中
        // 这时在程序运行时，会导致主动使用这个常量所在的类，显然会导致这个类被初始化
        System.out.println(MyParent3.str);
        // 输出
        // MyParent3 static code
        // d71fd705-5a12-4655-bf12-22cd1b7f6392
    }
}

class MyParent3 {
    public static final String str = UUID.randomUUID().toString();

    static {
        System.out.println("MyParent3 static code");
    }
}
