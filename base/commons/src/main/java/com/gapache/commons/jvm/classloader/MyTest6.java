package com.gapache.commons.jvm.classloader;

/**
 * @author HuSen
 * create on 2020/1/19 10:46
 */
public class MyTest6 {
    public static void main(String[] args) {
        // 使用类的静态方法 表示对类的主动使用 会进行初始化
        Singleton instance = Singleton.getInstance();
        // 从上往下进行初始化
        System.out.println("counter1: " + Singleton.counter1);
        System.out.println("counter2: " + Singleton.counter2);
        // counter1: 1
        // counter2: 0
    }
}

class Singleton {
    public static int counter1;

    private static Singleton singleton = new Singleton();

    public Singleton() {
        counter1++;
        // 准备阶段的重要意义
        counter2++;
    }

    public static int counter2 = 0;

    public static Singleton getInstance() {
        return singleton;
    }
}
