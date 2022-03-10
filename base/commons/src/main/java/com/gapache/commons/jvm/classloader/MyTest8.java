package com.gapache.commons.jvm.classloader;

/**
 * @author HuSen
 * create on 2020/1/20 10:51
 */
public class MyTest8 {
    public static void main(String[] args) {
        // 但是不代表一定不初始化父接口，在真正使用到父接口的时候（如引用接口中所定义的常量时），才会初始化
        System.out.println(MyParent8.thread);
    }
}

interface MyGradpa8 {
    Thread threadGradpa = new Thread() {
        {
            System.out.println("MyGradpa invoked");
        }
    };
}

interface MyParent8 extends MyGradpa8 {
    Thread thread = new Thread() {
        {
            System.out.println("MyParent5 invoked");
            System.out.println(threadGradpa);
        }
    };
}
