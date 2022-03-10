package com.gapache.commons.jvm.classloader;

/**
 * @author HuSen
 * create on 2020/3/14 5:45 下午
 */
public class MyTest15 {

    public static void main(String[] args) {
        System.out.println(System.getProperty("sun.boot.class.path"));
        System.out.println(System.getProperty("java.ext.dirs"));
        System.out.println(System.getProperty("java.class.path"));
    }
}
