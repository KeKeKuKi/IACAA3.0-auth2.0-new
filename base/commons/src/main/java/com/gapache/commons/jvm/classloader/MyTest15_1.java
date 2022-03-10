package com.gapache.commons.jvm.classloader;

/**
 * @author HuSen
 * create on 2020/3/14 5:52 下午
 */
public class MyTest15_1 {

    public static void main(String[] args) throws Exception {
        MyClassLoader loader1 = new MyClassLoader("/Users/macos/Desktop/", "loader1");

        Class<?> clazz = loader1.loadClass("com.gapache.commons.jvm.classloader.MyTest1");

        System.out.println("class: " + clazz.hashCode());
        System.out.println("class loader: " + clazz.getClassLoader());

        // 把MyTest1放到根类目录下加载
    }
}
