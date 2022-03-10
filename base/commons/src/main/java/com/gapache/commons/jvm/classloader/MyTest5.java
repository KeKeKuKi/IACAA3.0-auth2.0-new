package com.gapache.commons.jvm.classloader;

import java.util.Random;

/**
 * @author HuSen
 * create on 2020/1/20 10:41
 */
public class MyTest5 {
    public static void main(String[] args) {
        // 在初始化一个类时，并不会先初始化它所实现的接口
        System.out.println(MyChild5.b);
    }
}

interface MyParent5 {
    int a = new Random().nextInt();
    Thread thread = new Thread() {
        // 构造代码块 每个实例创建时都会执行一次
        {
            System.out.println("MyParent5 invoked");
        }
    };
}

class MyChild5 implements MyParent5 {
    public static int b = 5;
}
