package com.gapache.commons.jvm.classloader;

/**
 * @author HuSen
 * create on 2020/1/21 15:44
 */
public class MyTest11 {
    public static void main(String[] args) {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        System.out.println(classLoader);
        while (classLoader != null) {
            classLoader = classLoader.getParent();
            System.out.println(classLoader);
        }
        // sun.misc.Launcher$AppClassLoader@18b4aac2
        // sun.misc.Launcher$ExtClassLoader@135fbaa4
        // null
    }
}
