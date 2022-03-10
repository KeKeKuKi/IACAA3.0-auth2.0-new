package com.gapache.commons.jvm.classloader;

import java.lang.reflect.Method;

/**
 * @author HuSen
 * create on 2020/3/14 6:11 下午
 */
public class MyTest17 {

    public static void main(String[] args) throws Exception {
        MyClassLoader loader1 = new MyClassLoader("", "loader1");
        MyClassLoader loader2 = new MyClassLoader("", "loader2");

        Class<?> clazz1 = loader1.loadClass("com.gapache.commons.jvm.classloader.MyPerson");
        Class<?> clazz2 = loader2.loadClass("com.gapache.commons.jvm.classloader.MyPerson");

        System.out.println(clazz1 == clazz2);
        // true

        Object object1 = clazz1.newInstance();
        Object object2 = clazz2.newInstance();

        Method method = clazz1.getMethod("setMyPerson", Object.class);
        method.invoke(object1, object2);
    }
}
