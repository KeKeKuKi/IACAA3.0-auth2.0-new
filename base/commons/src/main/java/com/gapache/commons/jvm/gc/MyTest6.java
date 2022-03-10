package com.gapache.commons.jvm.gc;

/**
 * @author HuSen
 * create on 2020/5/7 11:28 上午
 */
public class MyTest6 {

    public static void main(String[] args) {
        // -verbose:gc -Xms10m -Xmx10m -XX:+UseG1GC -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:MaxGCPauseMillis=200m
        int size = 1024 * 1024;

        byte[] myAlloc1 = new byte[size];
        byte[] myAlloc2 = new byte[size];
        byte[] myAlloc3 = new byte[size];
        byte[] myAlloc4 = new byte[size];

        System.out.println("hello world");
    }
}
