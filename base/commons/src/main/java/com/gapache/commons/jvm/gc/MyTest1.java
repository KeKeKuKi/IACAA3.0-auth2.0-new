package com.gapache.commons.jvm.gc;

/**
 * @author HuSen
 * create on 2020/4/24 5:16 下午
 */
public class MyTest1 {

    public static void main(String[] args) {
        // -verbose:gc
        // -Xms20M 堆的初始大小
        // -Xmx20M 堆的最大大小
        // -Xmn10M 堆的新生代的大小
        // -XX:+PrintGCDetails 打印出垃圾回收的详细信息
        // -XX:SurvivorRatio=8 Eden空间与Survivor的比例是8:1 eden:to:from=8:1:1

        // PSYoungGen：Parallel Scavenge（新生代垃圾收集器）
        // ParOldGen：Parallel Old（老年代垃圾收集器）
        int size = 1024 * 1024;

        byte[] myAlloc1 = new byte[3 * size];
        byte[] myAlloc2 = new byte[3 * size];
        byte[] myAlloc3 = new byte[3 * size];
        byte[] myAlloc4 = new byte[3 * size];

        System.out.println("hello world");
    }
}
