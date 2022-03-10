package com.gapache.commons.jvm.classloader;

/**
 * @author HuSen
 * create on 2020/1/21 15:23
 */
public class MyTest10 {
    public static void main(String[] args) throws ClassNotFoundException {
        // 调用ClassLoader类的loadClass方法加载一个类，并不是对类的主动使用，不会导致初始化
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        Class<?> aClass = classLoader.loadClass("com.gapache.commons.jvm.classloader.CL");
        System.out.println(aClass);
        System.out.println("-------------------------");
        Class<?> aClass1 = Class.forName("com.gapache.commons.jvm.classloader.CL");
        System.out.println(aClass1);
        // class com.gapache.commons.jvm.classloader.CL
        // -------------------------
        // Class CL
        // class com.gapache.commons.jvm.classloader.CL
    }
}

class CL {
    static {
        System.out.println("Class CL");
    }
}
