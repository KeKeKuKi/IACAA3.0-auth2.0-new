package com.gapache.commons.jvm.classloader;

import java.util.Random;

/**
 * @author HuSen
 * create on 2020/1/20 10:48
 */
public class MyTest7 {
    public static void main(String[] args) {
        // 初始化一个接口时，并不要求其父接口都完成了初始化
        System.out.println(MyParent7.thread);
    }
}

interface MyGradpa {
    int a = new Random().nextInt();
    Thread thread = new Thread() {
        {
            System.out.println("MyGradpa invoked");
        }
    };
}

interface MyParent7 extends MyGradpa {
    Thread thread = new Thread() {
        {
            System.out.println("MyParent5 invoked");
        }
    };
}
