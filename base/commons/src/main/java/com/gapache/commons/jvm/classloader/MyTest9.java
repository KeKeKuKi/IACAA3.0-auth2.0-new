package com.gapache.commons.jvm.classloader;

/**
 * @author HuSen
 * create on 2020/1/21 11:47
 */
public class MyTest9 {
    public static void main(String[] args) throws ClassNotFoundException {
        // getClassLoader()方法了解
        // ClassLoader类解读
        Class<?> aClass = Class.forName("java.lang.String");
        System.out.println(aClass.getClassLoader());
        // null
        Class<?> aClass1 = Class.forName("com.gapache.commons.jvm.classloader.C");
        System.out.println(aClass1.getClassLoader());
        // sun.misc.Launcher$AppClassLoader@18b4aac2
    }
}

class C {

}
