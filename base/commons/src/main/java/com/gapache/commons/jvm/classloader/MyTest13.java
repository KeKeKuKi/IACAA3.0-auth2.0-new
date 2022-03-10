package com.gapache.commons.jvm.classloader;

/**
 * @author HuSen
 * create on 2020/1/24 16:51
 */
public class MyTest13 {

    public static void main(String[] args) {
        System.out.println("123".getClass().getClassLoader());
        // null说明是根类加载器加载
        System.out.println(MyTest13.class.getClassLoader());
        // sun.misc.Launcher$AppClassLoader@18b4aac2 应用类加载器加载
        System.out.println(Thread.currentThread().getContextClassLoader());
        // sun.misc.Launcher$AppClassLoader@18b4aac2 应用类加载器加载
    }
}
