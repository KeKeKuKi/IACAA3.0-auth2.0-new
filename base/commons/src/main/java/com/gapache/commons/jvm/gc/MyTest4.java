package com.gapache.commons.jvm.gc;

/**
 * @author HuSen
 * create on 2020/4/26 2:35 下午
 */
public class MyTest4 {

    public static void main(String[] args) throws InterruptedException {
        byte[] byte1 = new byte[512 * 1024];
        byte[] byte2 = new byte[512 * 1024];

        myGc();
        Thread.sleep(1000);

        System.out.println("111111");

        myGc();
        Thread.sleep(1000);

        System.out.println("222222");

        myGc();
        Thread.sleep(1000);

        System.out.println("333333");

        myGc();
        Thread.sleep(1000);

        System.out.println("444444");

        byte[] byte3 = new byte[1024 * 1024];
        byte[] byte4 = new byte[1024 * 1024];
        byte[] byte5 = new byte[1024 * 1024];

        myGc();
        Thread.sleep(1000);

        System.out.println("555555");

        myGc();
        Thread.sleep(1000);

        System.out.println("666666");

        System.out.println("hello world");
    }

    private static void myGc() {
        for (int i = 0; i < 40; i++) {
            byte[] bytes = new byte[1024 * 1024];
        }
    }
}
