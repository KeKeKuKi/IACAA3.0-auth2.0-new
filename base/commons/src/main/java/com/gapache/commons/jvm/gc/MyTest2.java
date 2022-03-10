package com.gapache.commons.jvm.gc;

/**
 * @author HuSen
 * create on 2020/4/26 11:15 上午
 */
public class MyTest2 {

    public static void main(String[] args) {
        // java -XX:+PrintCommandLineFlags -version 打印出默认的启动参数和版本
        // -XX:PretenureSizeThreshold=4194304 设置对象超过多大时直接在老年代进行分配
        // -XX:+UseSerialGC
        int size = 1024 * 1024;

        byte[] myAlloc = new byte[5 * size];

        try {
            Thread.sleep(1000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
